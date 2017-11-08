package xyz.danshin.smartnotes.repository;

import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import xyz.danshin.smartnotes.Enums;
import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.controller.ActivityController;
import xyz.danshin.smartnotes.ui.commons.NotePriority;

/**
 * Класс-репозиторий для приоритетов заметок
 */
public class NotePriorityRepository {
    /**
     * Репозиторий
     */
    private static final NotePriorityRepository ourInstance = new NotePriorityRepository();

    /**
     * Коллекция проритетов с доступом по ключу - значению перечисления
     */
    private final Map<Enums.NotePriority, NotePriority> prioritiesMap = new LinkedHashMap<>();

    /**
     * Коллекция проритетов
     */
    private final ArrayList<NotePriority> prioritiesList = new ArrayList<>();

    /**
     * Геттер для получения репозитория
     * @return Репозиторий с приоритетами заметок
     */
    public static NotePriorityRepository getInstance() {
        return ourInstance;
    }

    /**
     * Геттер для коллекции приоритетов с доступом по ключу - значению перечисления
     * @return Коллекция проритетов с доступом по ключу - значению перечисления
     */
    public Map<Enums.NotePriority, NotePriority> getPrioritiesMap() {
        return prioritiesMap;
    }

    /**
     * Геттер для коллекции приоритетов
     * @return Коллекция проритетов
     */
    public ArrayList<NotePriority> getPrioritiesList() {
        return prioritiesList;
    }


    /**
     * Конструктор
     */
    private NotePriorityRepository() {
        addPriorities();
        addPrioritiesToMap();
    }

    /**
     * Добавление приоритетов
     */
    private void addPriorities()
    {
        prioritiesList.add(new NotePriority(
                Enums.NotePriority.DEFAULT,
                ActivityController.getBaseActivity().getString(R.string.priority_default),
                ContextCompat.getDrawable(ActivityController.getBaseActivity(), R.drawable.drawable_dm_dot_white),
                ContextCompat.getDrawable(ActivityController.getBaseActivity(), R.drawable.drawable_priority_white_bg),
                ContextCompat.getColor(ActivityController.getBaseActivity(), R.color.priority_text_white),
                ContextCompat.getColor(ActivityController.getBaseActivity(), R.color.priority_bg_white)
        ));
        prioritiesList.add(new NotePriority(
                Enums.NotePriority.GREEN,
                ActivityController.getBaseActivity().getString(R.string.priority_green),
                ContextCompat.getDrawable(ActivityController.getBaseActivity(), R.drawable.drawable_dm_dot_green),
                ContextCompat.getDrawable(ActivityController.getBaseActivity(), R.drawable.drawable_priority_green_bg),
                ContextCompat.getColor(ActivityController.getBaseActivity(), R.color.priority_text_green),
                ContextCompat.getColor(ActivityController.getBaseActivity(), R.color.priority_bg_green)
        ));
        prioritiesList.add(new NotePriority(
                Enums.NotePriority.YELLOW,
                ActivityController.getBaseActivity().getString(R.string.priority_yellow),
                ContextCompat.getDrawable(ActivityController.getBaseActivity(), R.drawable.drawable_dm_dot_yellow),
                ContextCompat.getDrawable(ActivityController.getBaseActivity(), R.drawable.drawable_priority_yellow_bg),
                ContextCompat.getColor(ActivityController.getBaseActivity(), R.color.priority_text_yellow),
                ContextCompat.getColor(ActivityController.getBaseActivity(), R.color.priority_bg_yellow)
        ));
        prioritiesList.add(new NotePriority(
                Enums.NotePriority.RED,
                ActivityController.getBaseActivity().getString(R.string.priority_red),
                ContextCompat.getDrawable(ActivityController.getBaseActivity(), R.drawable.drawable_dm_dot_red),
                ContextCompat.getDrawable(ActivityController.getBaseActivity(), R.drawable.drawable_priority_red_bg),
                ContextCompat.getColor(ActivityController.getBaseActivity(), R.color.priority_text_red),
                ContextCompat.getColor(ActivityController.getBaseActivity(), R.color.priority_bg_red)
        ));
    }

    /**
     * Добавление приоритетов в коллекцию с доступом по ключу
     */
    private void addPrioritiesToMap()
    {
        for (NotePriority priority : prioritiesList)
            prioritiesMap.put(priority.getPriorityType(), priority);
    }
}
