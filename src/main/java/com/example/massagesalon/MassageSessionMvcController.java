/**
 * MVC-контроллер для работы с сеансами массажа.
 * Отвечает за рендеринг страниц и взаимодействие с пользователем через интерфейс MVC (Thymeleaf).
 */
package com.example.massagesalon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

@Controller
public class MassageSessionMvcController {

    private final MassageSessionService service;
    private final UserService userService;

    /**
     * Конструктор контроллера, внедряет сервисы для работы с сеансами и пользователями.
     *
     * @param service    сервис для управления сеансами массажа
     * @param userService сервис для управления пользователями
     */
    @Autowired
    public MassageSessionMvcController(MassageSessionService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    /**
     * Отображение главной страницы.
     *
     * @param model   модель для передачи данных в вид
     * @param keyword слово для поиска (необязательно)
     * @return имя шаблона "index"
     */
    @GetMapping("/")
    public String viewHomePage(Model model, @RequestParam(required = false) String keyword) {
        List<MassageSession> listSessions = service.listAll(keyword);
        model.addAttribute("listSessions", listSessions);
        model.addAttribute("keyword", keyword);
        model.addAttribute("serviceTypes", service.getAllServiceTypes());
        return "index";
    }

    /**
     * Страница для администратора с дополнительными функциями, включая статистику.
     *
     * @param model   модель
     * @param keyword слово для поиска
     * @return шаблон "admin/index"
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String viewAdminPage(Model model, @RequestParam(required = false) String keyword) {
        List<MassageSession> listSessions = service.listAll(keyword);
        model.addAttribute("listSessions", listSessions);
        model.addAttribute("keyword", keyword);
        model.addAttribute("serviceTypes", service.getAllServiceTypes());

        double averagePrice = listSessions.stream().mapToDouble(MassageSession::getPrice).average().orElse(0.0);
        int maxDuration = listSessions.stream().mapToInt(MassageSession::getDuration).max().orElse(0);
        int minDuration = listSessions.stream().mapToInt(MassageSession::getDuration).min().orElse(0);

        Map<String, Long> sessionCountByDate = listSessions.stream()
                .collect(Collectors.groupingBy(session -> session.getSessionDate().format(DateTimeFormatter.ISO_DATE), Collectors.counting()));

        Optional<User> mostFrequentClient = listSessions.stream()
                .collect(Collectors.groupingBy(MassageSession::getClient, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        long totalClients = listSessions.stream()
                .map(MassageSession::getClient)
                .distinct()
                .count();

        model.addAttribute("averagePrice", averagePrice);
        model.addAttribute("maxDuration", maxDuration);
        model.addAttribute("minDuration", minDuration);
        model.addAttribute("sessionCountByDate", sessionCountByDate);
        model.addAttribute("mostFrequentClient", mostFrequentClient.isPresent() ? mostFrequentClient.get().getUsername() : "Нет записей");
        model.addAttribute("totalClients", totalClients);

        return "admin/index";
    }

    /**
     * Страница со статистикой для администратора.
     *
     * @param model модель
     * @return шаблон "statistics"
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/statistics")
    public String viewStatisticsPage(Model model) {
        List<MassageSession> listSessions = service.listAll(null);

        double averagePrice = listSessions.stream()
                .mapToDouble(MassageSession::getPrice)
                .average()
                .orElse(0.0);

        int maxDuration = listSessions.stream()
                .mapToInt(MassageSession::getDuration)
                .max()
                .orElse(0);

        int minDuration = listSessions.stream()
                .mapToInt(MassageSession::getDuration)
                .min()
                .orElse(0);

        Map<String, Long> sessionCountByDate = listSessions.stream()
                .collect(Collectors.groupingBy(
                        session -> session.getSessionDate().format(DateTimeFormatter.ISO_DATE),
                        Collectors.counting()));
        Map<String, Long> sortedSessionCountByDate = new TreeMap<>(sessionCountByDate);

        Optional<User> mostFrequentClient = listSessions.stream()
                .collect(Collectors.groupingBy(MassageSession::getClient, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        long totalClients = listSessions.stream()
                .map(MassageSession::getClient)
                .distinct()
                .count();

        model.addAttribute("averagePrice", averagePrice);
        model.addAttribute("maxDuration", maxDuration);
        model.addAttribute("minDuration", minDuration);
        model.addAttribute("sessionCountByDate", sortedSessionCountByDate);
        model.addAttribute("mostFrequentClient", mostFrequentClient.isPresent() ? mostFrequentClient.get().getUsername() : "Нет записей");
        model.addAttribute("totalClients", totalClients);

        return "statistics";
    }

    /**
     * Страница "О нас".
     *
     * @return имя шаблона "about"
     */
    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }

    /**
     * Страница сеансов для терапевта, отображает только его собственные сеансы.
     *
     * @param model     модель
     * @param principal текущий аутентифицированный пользователь
     * @return шаблон "therapist/sessions"
     */
    @PreAuthorize("hasRole('THERAPIST')")
    @GetMapping("/therapist/sessions")
    public String therapistSessions(Model model, Principal principal) {
        String therapistName = principal.getName();
        List<MassageSession> listSessions = service.findByTherapist(therapistName);
        model.addAttribute("listSessions", listSessions);
        return "therapist/sessions";
    }

    /**
     * Страница сеансов для клиента, отображает только его собственные сеансы.
     *
     * @param model     модель
     * @param principal текущий аутентифицированный пользователь
     * @return шаблон "client/sessions"
     */
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/client/sessions")
    public String clientSessions(Model model, Principal principal) {
        String clientName = principal.getName();
        List<MassageSession> listSessions = service.findByClient(clientName);
        model.addAttribute("listSessions", listSessions);
        return "client/sessions";
    }

    /**
     * Добавляет в модель имя текущего пользователя, если он аутентифицирован.
     *
     * @param model     модель
     * @param principal текущий аутентифицированный пользователь
     */
    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
    }

    /**
     * Настройка преобразователей свойств для связей между сущностями.
     * Позволяет связывать поля формы с сущностями User и Enum MassageType.
     *
     * @param binder связыватель данных
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Преобразователь для masseur
        binder.registerCustomEditor(User.class, "masseur", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    User masseur = userService.findById(Long.parseLong(text));
                    setValue(masseur);
                }
            }
        });

        // Преобразователь для client
        binder.registerCustomEditor(User.class, "client", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    User client = userService.findById(Long.parseLong(text));
                    setValue(client);
                }
            }
        });

        // Преобразователь для MassageType
        binder.registerCustomEditor(MassageType.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    setValue(MassageType.valueOf(text));
                }
            }
        });
    }

    /**
     * Страница просмотра всех пользователей (только для ADMIN).
     *
     * @param model модель
     * @return шаблон "admin/users"
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public String viewUsersPage(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    /**
     * Форма создания нового сеанса массажа (только для ADMIN).
     *
     * @param model модель
     * @return шаблон "new_session"
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/new")
    public String showNewSessionForm(Model model) {
        model.addAttribute("massageSession", new MassageSession());
        model.addAttribute("clients", userService.findAllClients());
        model.addAttribute("masseurs", userService.findAllTherapists());
        model.addAttribute("massageTypes", service.getAllServiceTypes());
        return "new_session";
    }

    /**
     * Сохранение нового сеанса в базе данных (только для ADMIN).
     *
     * @param session сеанс для сохранения
     * @param result  результат валидации формы
     * @param model   модель
     * @return редирект на страницу "/admin" или повтор формы в случае ошибок
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/save")
    public String saveSession(@Valid @ModelAttribute("massageSession") MassageSession session, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clients", userService.findAllClients());
            model.addAttribute("masseurs", userService.findAllTherapists());
            model.addAttribute("massageTypes", service.getAllServiceTypes());
            return "new_session";
        }
        service.save(session);
        return "redirect:/admin";
    }

    /**
     * Форма редактирования существующего сеанса массажа (только для ADMIN).
     *
     * @param id    идентификатор сеанса
     * @param model модель
     * @return шаблон "edit_session"
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/edit/{id}")
    public String showEditSessionForm(@PathVariable Long id, Model model) {
        MassageSession session = service.get(id);
        if (session == null) {
            throw new ResourceNotFoundException("Сеанс с ID " + id + " не найден.");
        }
        model.addAttribute("massageSession", session);
        model.addAttribute("clients", userService.findAllClients());
        model.addAttribute("masseurs", userService.findAllTherapists());
        model.addAttribute("massageTypes", service.getAllServiceTypes());
        return "edit_session";
    }

    /**
     * Удаление сеанса массажа по идентификатору (только для ADMIN).
     *
     * @param id идентификатор сеанса
     * @return редирект на "/admin"
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/delete/{id}")
    public String deleteSession(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/admin";
    }
}
