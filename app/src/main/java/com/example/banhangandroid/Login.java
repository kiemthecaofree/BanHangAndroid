package com.example.banhangandroid;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.api.UserApi;
import com.example.model.User;
import com.example.model.UserLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    // Biến toàn cục
    private static String username_save = "";
    private static String password_save = "";

    private static boolean checkBox = false;

    public static User USER_LOGIN = null;
    Button btnSignIn;
    Boolean checkLogin = false;
    EditText txtUsername, txtPassword;

    CheckBox isRemember;

    TextView createAccount, forgotPassword;

    boolean passwordVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.v("haha", "VÀO LOGIN");
        addControls();
        addEvents();
    }


    private void addControls() {
        btnSignIn = findViewById(R.id.btnSignIn);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        createAccount = findViewById(R.id.createAccount);
        forgotPassword = findViewById(R.id.forgotPassword);
        isRemember = findViewById(R.id.isRemember);

        txtUsername.setText(username_save);
        txtPassword.setText(password_save);
        isRemember.setChecked(checkBox);
    }

    private void addEvents() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = false;
                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                if (username.length() == 0) {
                    openDialogLogin(isLogin, "Vui lòng nhập username");
                    return;
                }
                if (password.length() == 0) {
                    openDialogLogin(isLogin, "Vui lòng nhập password");
                    return;
                }

                handleLoginApi();
            }
        });

        // Show password
        txtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX() >= txtPassword.getRight() - txtPassword.getCompoundDrawables()[Right].getBounds().width()){
                        if(passwordVisible){
                            txtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_password, 0, R.drawable.show_password, 0);
                            txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }else{
                            txtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_password, 0, R.drawable.hide_password, 0);
                            txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                    }
                }
                return false;
            }
        });

        // createAccount

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        // forgotPassword
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword_EnterEmail.class);
                startActivity(intent);
            }
        });
    }

    public void openDialogLogin(boolean isLogin, String text_content) {
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
        if (isLogin) {
            txtDialogContent.setText(text_content);
        } else {
            txtDialogContent.setText("Đăng nhập không thành công! \n" + text_content);
        }
        btnDialogOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void handleLoginApi() {
        LoadingDialog loadingDialog = new LoadingDialog(Login.this);
        loadingDialog.startLoadingDialog();
        Log.e("txtUsername", txtUsername.getText().toString());
        Log.e("txtPassword", txtPassword.getText().toString());
        String u_name = txtUsername.getText().toString().trim().toLowerCase();
        String u_password = txtPassword.getText().toString().toLowerCase();
        UserLogin user = new UserLogin(u_name, u_password);
        UserApi.userAPI.postLogin(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e("CODE", String.valueOf(response.code()));
                if (response.code() == 200) {
                    checkLogin = true;
                    USER_LOGIN = response.body();
                    Log.v("USER", String.valueOf(USER_LOGIN));
                    openDialogLogin(true, "Đăng nhập thành công!");
                    // DANG NHAP THANH CONG CHUYEN MAN HINH O DAY
                    if (isRemember.isChecked()) {
                        username_save = u_name;
                        password_save = u_password;
                        checkBox = true;
                    } else {
                        username_save = "";
                        password_save = "";
                        checkBox = false;
                    }
                } else {
                    checkLogin = false;
                    USER_LOGIN = null;
                    openDialogLogin(false, "Tài khoản hoặc mật khẩu không chính xác!");
                }
                loadingDialog.closeLoadingDialog();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                checkLogin = false;
            }
        });
    }
}