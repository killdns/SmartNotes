package xyz.danshin.smartnotes.entity;

import android.text.format.DateFormat;

import java.io.Serializable;
import java.util.Date;

import xyz.danshin.smartnotes.Enums;
import xyz.danshin.smartnotes.repository.NoteRepository;

/**
 * Класс заметки
 */
public class Note implements Serializable, Comparable<Note> {

    /**
     * Id заметки
     */
    protected int id;

    /**
     * Заголовок заметки
     */
    protected String title;

    /**
     * Описание заметки
     */
    protected String description;

    /**
     * Приоритет заметки
     */
    protected Enums.NotePriority priority;

    /**
     * Дата последнего изменения заметки
     */
    protected Date lastModifiedDate;

    /**
     * Состояние выделения заметки
     */
    private boolean isSelected = false;

    /**
     * Формат даты-времени
     */
    private DateFormat dateFormat =  new android.text.format.DateFormat();

    /**
     * Конструктор класса заметки
     * @param title Заголовок заметки
     * @param desc Описание заметки
     * @param date Дата последнего изменения заметки
     */
    public Note (String title, String desc, Date date)
    {
        this.id = NoteRepository.getNextId();
        this.title = title;
        this.priority = Enums.NotePriority.DEFAULT;
        this.description = desc;
        this.lastModifiedDate = date == null ? new Date() : date;
    }

    /**
     * Геттер для Id заметки
     * @return Id заметки
     */
    public int getId() { return this.id; }

    /**
     * Сеттер для Id заметки
     */
    public void setId(int id) { this.id = id; }

    /**
     * Геттер для заголовка заметки
     * @return Заголовок заметки
     */
    public String getTitle() { return this.title; }

    /**
     * Сеттер для заголовка заметки
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Геттер для описания заметки
     * @return Описание заметки
     */
    public String getDescription() { return this.description; }

    /**
     * Сеттер для описания заметки
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Геттер для приоритета заметки
     * @return Приоритет заметки
     */
    public Enums.NotePriority getPriority() {
        return priority;
    }

    /**
     * Сеттер для приоритета заметки
     */
    public void setPriority(Enums.NotePriority priority) {
        this.priority = priority;
    }
    /**
     * Геттер для даты последненго изменения заметки
     * @return Дата последнего изменения
     */
    public Date getLastModifiedDate() { return this.lastModifiedDate; }

    /**
     * Геттер для даты последненго изменения заметки в читабельном виде
     * @return Дата последнего изменения в читабельном виде
     */
    public String getFormatedLastModifiedDate() { return dateFormat.format("dd MMMM yyyy, HH:mm:ss", this.lastModifiedDate).toString(); }

    /**
     * Сеттер для даты последненго изменения заметки
     */
    public void setLastModifiedDate(Date lastModifiedDate) { this.lastModifiedDate = lastModifiedDate == null ? new Date() : lastModifiedDate; }

    /**
     * Геттер для состояния выделения заметки
     * @return Состояние выделения заметки
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Сеттер для состояния выделения заметки
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * Метод сравнения на идентичность экземпляров
     * @param obj Объект
     * @return Результат сравнения
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if (!(obj instanceof Note))
            return false;

        Note itemCompare = (Note) obj;
        if(itemCompare.getId() == this.getId())
            return true;

        return false;
    }

    /**
     * Метод сравнения экземпляров класса. Необходим для сортировки коллекции
     * @param note Объект, с которым будет произведено сравнение
     * @return Результат сравнения
     */
    @Override
    public int compareTo(Note note)
    {
        //Обратная сортировка по времени и приоритету
        Enums.NotePriority p1, p2;
        p1 = note.getPriority();
        p2 = this.getPriority();
        if (p1.compareTo(p2) < 0)
            return  p1.compareTo(p2);

        Date n1, n2;
        n1 = note.getLastModifiedDate();
        n2 = this.getLastModifiedDate();
        return  n1.compareTo(n2);
    }

    /**
     * Метод преобразования в текстовое представление
     * @return Текстовое представление
     */
    @Override
    public String toString()
    {
     return new StringBuilder().append("Note. ")
             .append("Id = " + this.getId() + ", ")
             .append("Title = "  + this.getTitle() + ", ")
             .append("Description = "  + this.getDescription() + ", ")
             .append("Date = "  + this.getLastModifiedDate() + ".")
             .toString();
    }

    /**
     * Метод преобразования в текстовое представление для экспорта
     * @return Текстовое представление для экспорта
     */
    public String toExportString()
    {
        return new StringBuilder()
                .append("Заголовок:\t"  + this.getTitle() + "\r\n")
                .append("Описание:\t"  + this.getDescription() + "\r\n")
                .append("Дата:\t\t"  + this.getFormatedLastModifiedDate())
                .toString();
    }
}
