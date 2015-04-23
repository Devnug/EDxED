package edxed.nug.devnug.edxed;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * A placeholder fragment containing a simple view.
 */
public class FeedbackFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public RadioButton problemButton;
    public RadioButton suggestionButton;
    public Button sendButton;
    public EditText details;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FeedbackFragment newInstance(int sectionNumber) {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FeedbackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        suggestionButton = (RadioButton) rootView.findViewById(R.id.radioButton);
        problemButton = (RadioButton) rootView.findViewById(R.id.radioButton2);
        details = (EditText) rootView.findViewById(R.id.details);
        sendButton = (Button) rootView.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "pbreitwieser@hudsonhs.com", null));
                if(suggestionButton.isChecked())
                    intent.putExtra(Intent.EXTRA_SUBJECT, "EdxEdNYC Feedback - Suggestion");
                else
                    intent.putExtra(Intent.EXTRA_SUBJECT, "EdxEdNYC Feedback - Problem");
                intent.putExtra(Intent.EXTRA_TEXT, details.getText().toString());
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

