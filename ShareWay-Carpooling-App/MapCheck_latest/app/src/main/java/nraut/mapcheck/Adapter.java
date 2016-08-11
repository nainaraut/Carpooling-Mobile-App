package nraut.mapcheck;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by naina on 5/11/2016.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ContactViewHolder>{
    private static List<ListRowStruct> availableList;
    Context context;

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView profile_pic;
        TextView personName;
        TextView origin;
        TextView destination;
        TextView status;
        View v;

        public ContactViewHolder(View v) {
            super(v);
            this.v = v;
            profile_pic =  (ImageView) v.findViewById(R.id.profilePic);
            personName = (TextView) v.findViewById(R.id.name);
            origin = (TextView) v.findViewById(R.id.originValue);
            destination = (TextView) v.findViewById(R.id.destinationValue);
            status = (TextView)v.findViewById(R.id.StatusVal);
            Button send = (Button)v.findViewById(R.id.send);
            send.setText("Request");
            v.setOnClickListener(this);

            //on send request
            v.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getPosition();
                    ListRowStruct clickedrow = availableList.get(position);
                    sendRequest(v, clickedrow.getEmail());
                    status.setText("Pending");
                    Button send = (Button)v.findViewById(R.id.send);
                    send.setEnabled(false);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            ListRowStruct clickedrow = availableList.get(position);
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

        public void sendRequest(final View v, String requestedEmail){

            String loginEmail = new SharedPrefEmail(v.getContext()).getEmail("LoginEmail");
            String loginType = new SharedPrefEmail(v.getContext()).getEmail("type");
            RequestTable requestTable = new RequestTable();
            requestTable.setEmail(loginEmail);
            requestTable.setRequestedEmailAddress(requestedEmail);
            requestTable.setType(loginType);
            requestTable.setStatus("PENDING");

            Backendless.Persistence.save(requestTable, new AsyncCallback<RequestTable>() {
                @Override
                public void handleResponse(RequestTable requestTable) {
                    System.out.println("Success in entering records in RequestTable");
                    Button send = (Button)v.findViewById(R.id.send);
                    send.setClickable(false);
                }

                public void handleFault(BackendlessFault fault) {
                    System.out.println("Error in entering records in RequestTable");
                }
            });
        }
    }

    public  Adapter(List<ListRowStruct> availableList) {
        this.availableList = availableList;
    }


    @Override
    public int getItemCount() {
        return availableList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ListRowStruct ci = availableList.get(i);
        contactViewHolder.personName.setText(ci.getPersonName());
        contactViewHolder.origin.setText(ci.getOrigin());
        contactViewHolder.destination.setText(ci.getDestination());
        contactViewHolder.status.setText(ci.getStatus());
        if(ci.getStatus() != null){
            if(ci.getStatus().equals("PENDING")){
                contactViewHolder.v.findViewById(R.id.send).setEnabled(false);
            }
            if(ci.getStatus().equals("CONFIRMED")){
                contactViewHolder.v.findViewById(R.id.send).setEnabled(false);
                Button btn = (Button)contactViewHolder.v.findViewById(R.id.send);
                btn.setText("CONFIRMED");
            }
        }
        else{
            TextView status = (TextView)contactViewHolder.v.findViewById(R.id.StatusVal);
            status.setText("AVAILABLE");
        }
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
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_row_structure, viewGroup, false);
        context = viewGroup.getContext();

        return new ContactViewHolder(itemView);
    }

    // called by touch helper callback
    public void onItemDismissed(int position) {
        availableList.remove(position);
        notifyItemRemoved(position);
    }

    // called by touch helper callback
    public boolean onItemMove(int fromPosition, int toPosition) {
        ListRowStruct temp = availableList.get(fromPosition);
        availableList.set(fromPosition, availableList.get(toPosition));
        availableList.set(toPosition, temp);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
