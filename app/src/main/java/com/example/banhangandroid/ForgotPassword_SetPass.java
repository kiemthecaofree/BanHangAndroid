package com.example.banhangandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.api.UserApi;
import com.example.model.ChangePassword;
import com.example.model.User;
import com.example.model.UserLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword_SetPass extends AppCompatActivity {

    Button btnEnterNewPassword;
    EditText txtEnterNewPassword, txtEnterReNewPassword;

    ImageButton btnPrevious;

    boolean passwordVisible, rePasswordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_set_pass);
        ForgotPassword_EnterEmail.otpCode = -1;
        addControls();
        addEvents();
    }


    private void addEvents() {
        btnEnterNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = txtEnterNewPassword.getText().toString().trim();
                String reNewPassword = txtEnterReNewPassword.getText().toString().trim();
                if (newPassword.length() == 0) {
                    openDialogSetPass(false, "Vui lòng nhập mật khẩu mới!");
                    return;
                }
                if (reNewPassword.length() == 0) {
                    openDialogSetPass(false, "Vui lòng nhập lại mật khẩu mới!");
                    return;
                }

                if (newPassword.length() < 6) {
                    openDialogSetPass(false, "Mật khẩu cần từ 6 ký tự trở lên");
                    return;
                }

                if (newPassword.equals(reNewPassword) == false) {
                    openDialogSetPass(false, "Hai mật khẩu không giống nhau! Vui lòng nhập lại");
                    return;
                }

                handleLoginApi();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // Show password
        txtEnterNewPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX() >= txtEnterNewPassword.getRight() - txtEnterNewPassword.getCompoundDrawables()[Right].getBounds().width()){
                        if(passwordVisible){
                            txtEnterNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0);
                            txtEnterNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }else{
                            txtEnterNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_password, 0);
                            txtEnterNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                    }
                }
                return false;
            }
        });

        // Show RePassword
        txtEnterReNewPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX() >= txtEnterReNewPassword.getRight() - txtEnterReNewPassword.getCompoundDrawables()[Right].getBounds().width()){
                        if(rePasswordVisible){
                            txtEnterReNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0);
                            txtEnterReNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            rePasswordVisible = false;
                        }else{
                            txtEnterReNewPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_password, 0);
                            txtEnterReNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            rePasswordVisible = true;
                        }
                    }
                }
                return false;
            }
        });
    }

    public void handleLoginApi() {
        String email = ForgotPassword_EnterEmail.emailInput;
        String password = txtEnterNewPassword.getText().toString().trim();
        if (email.length() == 0) {
            openDialogSetPass(false, "Vui lòng quay lại bước nhập địa chỉ Email!");
            return;
        }
        if (!email.contains("@gmail.com")) {
            openDialogSetPass(false, "Email nhập vào không hợp lệ!\nVui lòng quay lại bước nhập địa chỉ Email.");
            return;
        }

        LoadingDialog loadingDialog = new LoadingDialog(ForgotPassword_SetPass.this);
        loadingDialog.startLoadingDialog();

        Log.e("email", email);
        Log.e("txtPassword", password);
        ChangePassword changePassword = new ChangePassword(email, password);
        UserApi.userAPI.postChangePassByEmail(changePassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        openDialogSetPass(true, "Thay đổi mật khẩu thành công!");
                    }
                } else {
                    try {
                        String strResponseBody = "";
                        strResponseBody = response.errorBody().string();
                        JSONObject messageObject = new JSONObject(strResponseBody);
                        openDialogSetPass(false, "Thay đổi mật khẩu thất bại!\n" + messageObject.get("message"));
                        Log.v("Error code 400", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                loadingDialog.closeLoadingDialog();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void addControls() {
        btnEnterNewPassword = findViewById(R.id.btnEnterNewPassword);
        txtEnterNewPassword = findViewById(R.id.txtEnterNewPassword);
        txtEnterReNewPassword = findViewById(R.id.txtEnterReNewPassword);
        btnPrevious = findViewById(R.id.btnPrevious);
    }


    public void openDialogSetPass(Boolean isSuccess, String text_content) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_message);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);

        Button btnDialogOke = dialog.findViewById(R.id.btnDialogOke);
        TextView txtDialogContent = dialog.findViewById(R.id.txtDialogContent);
        txtDialogContent.setText(text_content);
        btnDialogOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSuccess) {
                    Intent intent = new Intent(ForgotPassword_SetPass.this, Login.class);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}