package xyz.danshin.smartnotes.entity;

import java.io.Serializable;
import java.util.Date;

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
     * Дата последнего изменения заметки
     */
    protected Date lastModifiedDate;

    /**
     * Состояние выделения заметки
     */
    private boolean isSelected = false;

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
        this.description = desc;
        this.lastModifiedDate = date == null ? new Date() : date;
    }

    /**
     * Геттер для Id заметки
     * @return
     */
    public int getId() { return this.id; }

    /**
     * Сеттер для Id заметки
     * @return
     */
    public void setId(int id) { this.id = id; }

    /**
     * Геттер для заголовка заметки
     * @return
     */
    public String getTitle() { return this.title; }

    /**
     * Сеттер для заголовка заметки
     * @return
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Геттер для описания заметки
     * @return
     */
    public String getDescription() { return this.description; }

    /**
     * Сеттер для описания заметки
     * @return
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Геттер для даты последненго изменения заметки
     * @return
     */
    public Date getLastModifiedDate() { return this.lastModifiedDate; }

    /**
     * Сеттер для даты последненго изменения заметки
     * @return
     */
    public void setLastModifiedDate(Date lastModifiedDate) { this.lastModifiedDate = lastModifiedDate == null ? new Date() : lastModifiedDate; }

    /**
     * Геттер для состояния выделения заметки
     * @return
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Сеттер для состояния выделения заметки
     * @return
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
        //Обратная сортировка по времени
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
}
