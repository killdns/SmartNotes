package xyz.danshin.smartnotes.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import xyz.danshin.smartnotes.R;
import xyz.danshin.smartnotes.interfaces.IRvPriorityClickListener;

/**
 * ViewHolder для recyclerView с приоритетами заметок в общем меню
 */
public class RvPriorityTopDMViewHolder extends RecyclerView.ViewHolder {

    /**
     * Кнопка с картинкой
     */
    public ImageButton imageButton;

    /**
     * Объект, реализующий интерфейс IRvPriorityClickListener
     */
    private IRvPriorityClickListener priorityClickListener;

    /**
     * OnClickListener для элемета приоритета
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            priorityClickListener.onPriorityItemClick(v, getAdapterPosition());
        }
    };

    /**
     * Конструктор
     */
    public RvPriorityTopDMViewHolder(View view, IRvPriorityClickListener priorityClickListener) {
        super(view);
        this.imageButton = (ImageButton) view.findViewById(R.id.dm_priority_top_menu_item_image_button);
        this.priorityClickListener = priorityClickListener;

        imageButton.setOnClickListener(onClickListener);
    }

}
