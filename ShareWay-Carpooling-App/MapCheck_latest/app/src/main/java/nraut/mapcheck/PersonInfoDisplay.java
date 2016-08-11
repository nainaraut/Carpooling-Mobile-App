package nraut.mapcheck;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by naina on 5/22/2016.
 */
public class PersonInfoDisplay extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_person_info);

        Intent intent = getIntent();
        String pic = intent.getStringExtra("pic");
        String name = intent.getStringExtra("name");
        String origin = intent.getStringExtra("origin");
        String dest = intent.getStringExtra("dest");
        String email = intent.getStringExtra("email");
        final String phone = intent.getStringExtra("phone");
        String status = intent.getStringExtra("status");

        ImageView img = (ImageView)findViewById(R.id.pic);
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open(pic);
            img.setImageDrawable(Drawable.createFromStream(inputStream, null));
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView name1 = (TextView)findViewById(R.id.PersonName);
        name1.setText(name);

        TextView origin1 = (TextView)findViewById(R.id.OriginVal);
        origin1.setText(origin);

        TextView dest1 = (TextView)findViewById(R.id.DestinationVal);
        dest1.setText(dest);

        TextView email1 = (TextView)findViewById(R.id.EmailVal);
        email1.setText(email);

        TextView phone1 = (TextView)findViewById(R.id.phone);
        phone1.setText(phone);

        TextView status1 = (TextView)findViewById(R.id.status1);
        status1.setText(status);

        findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });

        findViewById(R.id.email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView email = (TextView)v.findViewById(R.id.EmailVal);
                String email1 = email.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, email1);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                try {
                    startActivity(Intent.createChooser(intent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(PersonInfoDisplay.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
