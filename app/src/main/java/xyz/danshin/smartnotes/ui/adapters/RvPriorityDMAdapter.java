package xyz.danshin.smartnotes.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import xyz.danshin.smartnotes.Enums;
import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.interfaces.IRvPriorityClickListener;
import xyz.danshin.smartnotes.repository.NotePriorityRepository;
import xyz.danshin.smartnotes.ui.commons.NotePriority;
import xyz.danshin.smartnotes.ui.viewholders.RvPriorityDMViewHolder;

/**
 * Адаптер заметок для recyclerView
 */
public class RvPriorityDMAdapter extends RecyclerView.Adapter<RvPriorityDMViewHolder> {

    /**
     * Активный приоритет
     */
    private Enums.NotePriority activePriority;

    /**
     * Объект, реализующий интерфейс обработки кликов
     */
    private IRvPriorityClickListener priorityClickListener;

    /**
     * Коллекция с приоритетами
     */
    private ArrayList<NotePriority> priorities = NotePriorityRepository.getInstance().getPrioritiesList();

    /**
     * Конструктор
     */
    public RvPriorityDMAdapter(IRvPriorityClickListener priorityClickListener) {
        this.priorityClickListener = priorityClickListener;
    }

    /**
     * Пролучение количества приоритетов
     * @return Количество приоритетов
     */
    @Override
    public int getItemCount() {
        return priorities.size();
    }

    /**
     * Метод, вызываемый при создании ViewHolder'a
     */
    @Override
    public RvPriorityDMViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dm_rv_item_priority, viewGroup, false);
        return new RvPriorityDMViewHolder(view1, priorityClickListener);
    }

    /**
     * Метод, вызываемый при привязке ViewHolder'a
     */
    @Override
    public void onBindViewHolder(RvPriorityDMViewHolder viewholder, int i) {
        viewholder.preview.setBackground(priorities.get(i).getDialogMenuItemBackground());
        viewholder.preview.setTextColor(priorities.get(i).getTextColor());
        viewholder.description.setText(priorities.get(i).getName());
        viewholder.cardView.setSelected(priorities.get(i).getPriorityType() == activePriority);

    }

    /**
     * Установка активного приоритета
     * @param activePriority Приоритет
     */
    public void setActivePriority (Enums.NotePriority activePriority) {this.activePriority = activePriority;}
}
