//package com.hardygtw.travelmemories.fragments.Nearby;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.hardygtw.travelmemories.activity.MapActivity;
//import com.hardygtw.travelmemories.model.NearbyPlaceItem;
//import com.hardygtw.travelmemories.R;
//
//
///**
// * Created by beaumoaj on 13/01/15.
// */
//public class RssItemFragment extends Fragment {
//    protected static final int REQUEST_CONTACT = 1;
//    private NearbyPlaceItem rssItem;
//    private TextView contactName;
//
//    public RssItemFragment() {
//        super();
//
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //rssItem = new RssItem();
//        //int position = getActivity().getIntent().getIntExtra("POSITION", 0);
//        int position = getArguments().getInt("POSITION");
//        //rssItem = TheNews.getInstance().getItem(position);
//        this.setHasOptionsMenu(true);
//       // getActivity().setTitle("From: " + TheNews.getInstance().getFeed().getTitle());
//        // Can't do this in the ViewPager, because it creates 3 fragments ready for scrolling
//        // and the user may not read them all
//        // rssItem.setRead();
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        //inflater.inflate(R.menu.feed_list_mgr, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_add_feed:
//                //Intent intent = new Intent(getActivity(), RssFeedManagerActivity.class);
//                //startActivityForResult(intent, 0);
//                // see end of chapter 10 for how to wire this up
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    /**
//     * Factory method to generate a fragment with arguments.  Cannot do this as a
//     * normal constructor because it interferes with the way Fragments are constructed
//     * and restarted in life cycle events.
//     * @param position the position of the item in the list to be displayed
//     * @return a new RssItemFragment
//     */
//    public static RssItemFragment newInstance(int position) {
//        Bundle args = new Bundle();
//        args.putInt("POSITION", position);
//        RssItemFragment fragment = new RssItemFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_rss_item, parent, false);
//        TextView title = (TextView) v.findViewById(R.id.title);
//        title.setText(rssItem.getTitle());
//		/*
//		 * TextView desc = (TextView) v.findViewById(R.id.description);
//		 * desc.setText(rssItem.getDescription());
//		 */
//        WebView desc = (WebView) v.findViewById(R.id.description);
//
//        String type;
//        // possible bugs to fix... the encoding might not be UTF-8
//        // and type might not be text/html
//        desc.loadData(rssItem.getDescription(), "text/html", "UTF-8");
//        TextView link = (TextView) v.findViewById(R.id.link);
//        link.setText(rssItem.getLink().toString());
//        TextView date = (TextView) v.findViewById(R.id.date);
//        date.setText(rssItem.getDate());
//
//        Button sendButton = (Button) v.findViewById(R.id.send_to_contact);
//        sendButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, rssItem.getSummary());
//                i.putExtra(Intent.EXTRA_SUBJECT, rssItem.getTitle());
//                startActivity(i);
//            }
//        });
//
//        Button shareButton = (Button) v.findViewById(R.id.choose_contact);
//        shareButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_PICK,
//                        ContactsContract.Contacts.CONTENT_URI);
//                startActivityForResult(i, REQUEST_CONTACT);
//            }
//        });
//
//        Button mapButton = (Button) v.findViewById(R.id.map_button);
//        if (rssItem.getLocation() == null) {
//            mapButton.setActivated(false);
//        } else {
//            mapButton.setActivated(true);
//            mapButton.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(getActivity(), MapActivity.class);
//                    i.putExtra(MapActivity.ITEM, rssItem);
//                    startActivity(i);
//                }
//            });
//        }
//
//        return v;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // http://mobile.tutsplus.com/tutorials/android/android-essentials-using-the-contact-picker/
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case RssItemFragment.REQUEST_CONTACT:
//                    Cursor cursor = null;
//                    String email = "";
//                    String name = "";
//                    try {
//                        Uri result = data.getData();
//
//                        // get the contact id from the Uri
//                        String id = result.getLastPathSegment();
//                        // query for everything email
//                        cursor = getActivity().getContentResolver().query(
//                                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?",
//                                new String[] { id }, null);
//                        int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
//                        int nameIdx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//                        // let's just get the first email
//                        if (cursor.moveToFirst()) {
//                            email = cursor.getString(emailIdx);
//                            name = cursor.getString(nameIdx);
//
//
//                        } else {
//                           // Log.i(RssItemActivity.TAG, "No results");
//                        }
//                    } catch (Exception e) {
//                        //Log.i(RssItemActivity.TAG, "Failed to get email data", e);
//                    } finally {
//                        if (cursor != null) {
//                            cursor.close();
//                        }
//                        if (email.length() == 0) {
//                            Toast.makeText(getActivity(),
//                                    "No email found for contact.",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    break;
//            }
//        }
//    }
//}