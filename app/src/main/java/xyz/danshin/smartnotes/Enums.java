package xyz.danshin.smartnotes;


/**
 * Класс, содержащий различные перечисления
 */
public final class Enums {
    /**
     * Пречисление с типами операций с заметками
     */
    public enum NoteActivityType {
        VIEW,
        CREATE,
        EDIT
    }

    /**
     * Пречисление с типами полей. Используется при редактирвании заметки и обозначает адреса полей, расположенные во временном массиве
     */
    public enum NoteTempData {
        TITLE,
        DESCRIPTION,
        PRIORITY
    }

    /**
     * Пречисление с приоритетами заметок
     */
    public enum NotePriority {
        DEFAULT,
        GREEN,
        YELLOW,
        RED
    }
}
