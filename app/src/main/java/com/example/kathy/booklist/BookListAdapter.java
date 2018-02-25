package com.example.kathy.booklist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kathy on 11/22/2017.
 */

public class BookListAdapter extends ArrayAdapter<bookInfo> {

    private static final String LOG_TAG = BookListAdapter.class.getSimpleName();


    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context     The current context. Used to inflate the layout file.
     * @param bookInfos A List of AndroidFlavor objects to display in a list
     */
    public BookListAdapter(Activity context, List<bookInfo> bookInfos) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, bookInfos);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        bookInfo currentbookInfo = getItem(position);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.


        // Find the TextView with view ID title
        TextView titleView = (TextView) listItemView.findViewById(R.id.book_title);
        titleView.setText(currentbookInfo.getTitle());

        // Find the TextView with view ID author
        TextView authorView = (TextView) listItemView.findViewById(R.id.book_author);
        authorView.setText(currentbookInfo.getAuthor());


        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

}

