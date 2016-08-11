package nraut.mapcheck;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by naina on 5/24/2016.
 */
public class Adapter2 extends RecyclerView.Adapter<Adapter2.ContactViewHolder2>{

    private static List<ListRowStruct2> availableList;
    Context context;

    public static class ContactViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView profile_pic;
        TextView personName;
        TextView origin;
        TextView destination;
        TextView status;
        View v;

        public ContactViewHolder2(View v) {
            super(v);
            this.v = v;
            profile_pic = (ImageView) v.findViewById(R.id.profilePic2);
            personName = (TextView) v.findViewById(R.id.name2);
            origin = (TextView) v.findViewById(R.id.originValue2);
            destination = (TextView) v.findViewById(R.id.destinationValue2);
            status = (TextView) v.findViewById(R.id.StatusVal2);
            final ImageView cnf = (ImageView)v.findViewById(R.id.confirm);
            Button send = (Button)v.findViewById(R.id.send2);
            send.setText("Confirm");
            v.setOnClickListener(this);

            //on confirm request
            v.findViewById(R.id.send2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getPosition();
                    ListRowStruct2 clickedrow = availableList.get(position);
                    sendConfirmation(v, clickedrow.getEmail());
                    status.setText("Confirmed");
                    Button send = (Button)v.findViewById(R.id.send2);
                    send.setEnabled(false);
                    cnf.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            ListRowStruct2 clickedrow = availableList.get(position);
            Intent intent = new Intent(v.getContext(), PersonInfoDisplay.class);
            intent.putExtra("pic", clickedrow.getProfile_pic());
            intent.putExtra("name", clickedrow.getPersonName());
            intent.putExtra("origin", clickedrow.getOrigin());
            intent.putExtra("dest", clickedrow.getDestination());
            intent.putExtra("email", clickedrow.getEmail());
            intent.putExtra("phone", clickedrow.getPhone());
            intent.putExtra("status", clickedrow.getStatus());
            v.getContext().startActivity(intent);
        }

        //send confirmation to both driver and passenger
        public void sendConfirmation(final View v, String requestedEmail){
            String loginEmail = new SharedPrefEmail(v.getContext()).getEmail("LoginEmail");
            String whereClause = "email='"+requestedEmail+"' and requestedEmailAddress = '"+loginEmail+"'";
            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            dataQuery.setWhereClause(whereClause);

            //get the existing record
            Backendless.Data.of(RequestTable.class).find(dataQuery, new AsyncCallback<BackendlessCollection<RequestTable>>() {
                @Override
                public void handleResponse(BackendlessCollection<RequestTable> requestTableBackendlessCollection) {
                    Iterator<RequestTable> iterater = requestTableBackendlessCollection.getCurrentPage().iterator();
                    if (iterater.hasNext()) {
                        RequestTable requestTable = iterater.next();
                        requestTable.setStatus("CONFIRMED");

                        //update the existing record with confirmed status
                        Backendless.Persistence.save(requestTable, new AsyncCallback<RequestTable>() {
                            @Override
                            public void handleResponse(RequestTable requestTable) {
                                System.out.println("Success in changing status records in RequestTable");
                            }

                            public void handleFault(BackendlessFault fault) {
                                System.out.println("Error in changing status records in RequestTable");
                            }
                        });
                    }
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    System.out.println("Errorrrr in adapter 2 RequestTable");
                }
            });
        }
    }

    public  Adapter2(List<ListRowStruct2> availableList) {
        this.availableList = availableList;
    }

    @Override
    public ContactViewHolder2 onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_row_structure2, viewGroup, false);
        context = viewGroup.getContext();

        return new ContactViewHolder2(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder2 contactViewHolder, int i) {
        ListRowStruct2 ci = availableList.get(i);
        contactViewHolder.personName.setText(ci.getPersonName());
        contactViewHolder.origin.setText(ci.getOrigin());
        contactViewHolder.destination.setText(ci.getDestination());
        contactViewHolder.status.setText(ci.getStatus());
        if(ci.getStatus() != null){
            if(ci.getStatus().equals("CONFIRMED")){
                contactViewHolder.v.findViewById(R.id.send2).setEnabled(false);
                contactViewHolder.v.findViewById(R.id.confirm).setVisibility(View.VISIBLE);
            }
        }
        Button confirm = (Button)contactViewHolder.v.findViewById(R.id.send2);
        confirm.setText("Confirm");
        try {
            String imageFileName = ci.getProfile_pic();
            InputStream inputStream = context.getAssets().open(imageFileName);
            contactViewHolder.profile_pic.setImageDrawable(Drawable.createFromStream(inputStream, null));
            contactViewHolder.profile_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return availableList.size();
    }

    // called by touch helper callback
    public void onItemDismissed(int position) {
        availableList.remove(position);
        notifyItemRemoved(position);
    }

    // called by touch helper callback
    public boolean onItemMove(int fromPosition, int toPosition) {
        ListRowStruct2 temp = availableList.get(fromPosition);
        availableList.set(fromPosition, availableList.get(toPosition));
        availableList.set(toPosition, temp);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
