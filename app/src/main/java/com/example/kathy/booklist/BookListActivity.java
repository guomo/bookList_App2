package com.example.kathy.booklist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gman.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<bookInfo>>,Parcelable {
    /*
      * URL for Google Books API
      */
    private final static String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private String mUrl;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    private static final String STATE_ITEMS = "items";

    private ProgressBar mProgress;

    /**
     * Constant value for the bookInfo loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOKLISTING_LOADER_ID = 1;

    /**
     * Adapter for the list of bookInfos
     */
    private BookListAdapter mAdapter;

    public static final String LOG_TAG = BookListActivity.class.getName();

    ArrayList<String> m_listItems = new ArrayList<String>();


    private ArrayList<bookInfo> books;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);


        if(savedInstanceState == null || !savedInstanceState.containsKey("bookList")) {
            Log.i(LOG_TAG, "create a new");
            books = new ArrayList<bookInfo>();
//            books = new ArrayList(Arrays.asList(books));
        } else {
            Log.i(LOG_TAG, "return old");
            // books = savedInstanceState.getParcelableArrayList("bookList");
        }

        //Set progress spinner visible in bookInfo_activity.xml
        mProgress = findViewById(R.id.loading_spinner);
        mProgress.setVisibility(View.VISIBLE);


        // Find a reference to the {@link ListView} in the layout
        final ListView bookInfoListView = findViewById(R.id.list);


        //Find reference to set layout to empty state Textview
        mEmptyStateTextView = findViewById(R.id.empty_view);
        bookInfoListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of bookInfos as input
        mAdapter = new BookListAdapter(this, new ArrayList<bookInfo>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface

        Button searchButton = (Button) findViewById(R.id.search_button);
        final EditText searchTextView = (EditText) findViewById(R.id.search_text);

        getLoaderManager().initLoader(BOOKLISTING_LOADER_ID, null, BookListActivity.this);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAdapter.clear();

                //* get user input and set it ready for url
                String keyWord = searchTextView.getText().toString().trim();
                keyWord = keyWord.replaceAll(" +", "+");
                //* make url
                mUrl = BASE_URL + keyWord;
                Toast.makeText(BookListActivity.this, mUrl, Toast.LENGTH_SHORT).show();
                //* make UI look for loading books

                mProgress.setVisibility(View.VISIBLE);

                //*close keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                // Create a new adapter that takes an empty list of bookInfos as input
                mAdapter = new BookListAdapter(BookListActivity.this, new ArrayList<bookInfo>());

                // Set the adapter on the {@link ListView}
                // so the list can be populated in the user interface
                bookInfoListView.setAdapter(mAdapter);

                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (isConnected == true) {
                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    LoaderManager loaderManager = getLoaderManager();

                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    getLoaderManager().restartLoader(BOOKLISTING_LOADER_ID, null, BookListActivity.this);
                } else {
                    //Turn off spinner
                    mProgress.setVisibility(View.GONE);

                    // Set empty state text to display "No bookInfos found."
                    mEmptyStateTextView.setText(R.string.No_Connection);

                }

            }
        });


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("bookList", books);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        books = savedState.getParcelableArrayList("bookList");
    }


    @Override
    public Loader<List<bookInfo>> onCreateLoader(int i, Bundle bundle) {
        return new BookListLoader(this, mUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<bookInfo>> loader, List<bookInfo> bookInfos) {

        //Turn off spinner
        mProgress.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.empty);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (bookInfos != null && !bookInfos.isEmpty()) {
            // Avoid NullPointerException
            books = new ArrayList<>();
            // Update our storage List object
            books.addAll(bookInfos);
            // Update our display List object
            mAdapter.addAll(bookInfos);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<bookInfo>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUrl);
        dest.writeStringList(this.m_listItems);
        dest.writeList(this.books);
    }

    public BookListActivity() {
    }

    protected BookListActivity(Parcel in) {
        this.mUrl = in.readString();
        this.mEmptyStateTextView = in.readParcelable(TextView.class.getClassLoader());
        this.mProgress = in.readParcelable(ProgressBar.class.getClassLoader());
        this.mAdapter = in.readParcelable(BookListAdapter.class.getClassLoader());
        this.m_listItems = in.createStringArrayList();
        this.books = new ArrayList<bookInfo>();
        in.readList(this.books, bookInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<BookListActivity> CREATOR = new Parcelable.Creator<BookListActivity>() {
        @Override
        public BookListActivity createFromParcel(Parcel source) {
            return new BookListActivity(source);
        }

        @Override
        public BookListActivity[] newArray(int size) {
            return new BookListActivity[size];
        }
    };
}
