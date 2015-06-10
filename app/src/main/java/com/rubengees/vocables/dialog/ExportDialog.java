package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;
import com.rubengees.vocables.utils.ExportTask;

import java.io.File;

/**
 * Created by ruben on 29.05.15.
 */
public class ExportDialog extends DialogFragment implements ExportTask.OnExportFinishedListener {

    private ExportTask task;

    private MaterialDialog dialog;
    private File file;

    public static ExportDialog newInstance(String path) {
        ExportDialog dialog = new ExportDialog();
        Bundle bundle = new Bundle();

        bundle.putString("path", path);
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file = new File(getArguments().getString("path"));

        task = ExportTask.getInstance(getActivity(), file, this);

        task.startIfNotRunning();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        dialog = builder.title(getActivity().getString(R.string.export_title)).progress(true, 100).content(getActivity().getString(R.string.dialog_export_content))
                .negativeText(getActivity().getString(R.string.dialog_cancel)).callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        task.cancel();
                    }
                }).build();

        return dialog;
    }

    @Override
    public void onExportFinished(String success) {
        Toast.makeText(getActivity(), success == null ? getActivity().getString(R.string.dialog_export_finish_message) : success, Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
