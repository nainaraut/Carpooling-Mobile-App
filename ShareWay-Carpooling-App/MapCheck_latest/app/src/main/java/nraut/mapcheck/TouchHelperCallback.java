package nraut.mapcheck;

/**
 * Created by naina on 4/22/2016.
 */import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by jsun on 4/17/2016.
 */
public class TouchHelperCallback extends ItemTouchHelper.Callback {

    Adapter mAdapter;

    public TouchHelperCallback(Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Set movement flags based on the layout manager
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        Log.i("nraut", "item moved from " + source.getAdapterPosition() + " to " + target.getAdapterPosition());
        return mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.i("nraut", "item at " + viewHolder.getAdapterPosition() + " is swiped away");
        mAdapter.onItemDismissed(viewHolder.getAdapterPosition());
    }
}
