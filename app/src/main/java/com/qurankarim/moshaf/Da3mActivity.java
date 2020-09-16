package com.qurankarim.moshaf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static com.qurankarim.moshaf.R.layout.activity_da3m;

public class Da3mActivity extends AppCompatActivity {

    private Button purchaseBtn, adBtnSimple;
    private RewardedAd rewardedAd;
    private AdView adView;

    private LoadingActivity loadingActivity;

    private static final String TAG = "Da3mActivity";

    private TextView userPoints;
    private UserData userData = new UserData();

    private long userPointsNumber;

    private TextView userNameFirst, userNameSecond, userNameThird;
    private TextView userPointsFirst, userPointsSecond, userPointsThird;
    private TextView userNameText;

    private CardView editUserDataCard;

    private ImageView refreshContribution;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_da3m);

        adView = findViewById(R.id.adViewMain);
        adBtnSimple = findViewById(R.id.ad_simple);
        purchaseBtn = findViewById(R.id.purchase_btn);
        userPoints = findViewById(R.id.user_points);

        userNameFirst = findViewById(R.id.user_name_first);
        userNameSecond = findViewById(R.id.user_name_second);
        userNameThird = findViewById(R.id.user_name_third);

        userPointsFirst = findViewById(R.id.user_points_first);
        userPointsSecond = findViewById(R.id.user_points_second);
        userPointsThird = findViewById(R.id.user_points_third);

        editUserDataCard = findViewById(R.id.go_to_edit_data);

        userNameText = findViewById(R.id.user_name_text);

        refreshContribution = findViewById(R.id.refresh_contribution);

        editUserDataCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditDialog();
            }
        });

        refreshContribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTopUsers();
            }
        });

        // get user points

        App.mDb.collection("users")
                .whereEqualTo("userAndroidId", App.androidId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        if (documentSnapshots.size() > 0) {
                            DocumentSnapshot documentSnapshot = documentSnapshots.get(0);
                            DocumentReference userDoc = documentSnapshot.getReference();

                            userDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (value.exists()) {
                                        userPointsNumber = value.getLong("userPoints");
                                        userPoints.setText("نقاطك : " + value.getLong("userPoints"));
                                        userNameText.setText(value.getString("userName"));

                                    }
                                }
                            });
                        }
                    }
                });


        getTopUsers();


        loadingActivity = new LoadingActivity(Da3mActivity.this);

        MobileAds.initialize(this, App.AppAdId);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        purchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openDialog();
                Toast.makeText(Da3mActivity.this, "قريبا", Toast.LENGTH_SHORT).show();
            }
        });

        adBtnSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });

        // Native Ad
        AdLoader adLoader = new AdLoader.Builder(this, App.nativeAdId)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().build();

                        TemplateView template = findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(unifiedNativeAd);

                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }

    public void openEditDialog() {
        EditUserDialoge exampleDialog = new EditUserDialoge();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void openDialog() {
        PurchaseDialogeActivity purchaseDialogeActivity = new PurchaseDialogeActivity();
        purchaseDialogeActivity.show(getSupportFragmentManager(), "Purchase dialog");
    }

    // da3m Ad
    @SuppressLint("MissingPermission")
    public void loadAd() {
        this.rewardedAd = new RewardedAd(this, App.da3mAdId);
        loadingActivity.startLoadingActivity();
        RewardedAdLoadCallback callback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                super.onRewardedAdFailedToLoad(loadAdError);
                loadingActivity.dismissDailog();
                Log.d(TAG, "onRewardedAdFailedToLoad");
            }

            @Override
            public void onRewardedAdLoaded() {
                super.onRewardedAdLoaded();
                loadingActivity.dismissDailog();
                showAd();
                Log.d(TAG, "onRewardedAdLoaded");
            }
        };
        this.rewardedAd.loadAd(new AdRequest.Builder().build(), callback);
    }

    public void showAd() {
        if (this.rewardedAd.isLoaded()) {
            RewardedAdCallback rewardedAdCallback = new RewardedAdCallback() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    Log.d(TAG, "onUserEarnedReward");
                }

                @Override
                public void onRewardedAdOpened() {
                    super.onRewardedAdOpened();
                    Log.d(TAG, "onRewardedAdOpened");
                }

                @Override
                public void onRewardedAdClosed() {
                    super.onRewardedAdClosed();
                    // user show Ad
                    App.mDb.collection("users").document(App.userDocId).update("userPoints", userPointsNumber += 10);
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    super.onRewardedAdFailedToShow(adError);
                    Log.d(TAG, "onRewardedAdFailedToShow");
                }
            };
            this.rewardedAd.show(this, rewardedAdCallback);
        } else {
            Log.d(TAG, "Filled to load ad");
        }
    }

    // get top users
    private void getTopUsers() {
        App.mDb.collection("users")
                .orderBy("userPoints", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        if (documentSnapshots.size() >= 3) {
                            DocumentSnapshot first = documentSnapshots.get(0);
                            DocumentSnapshot second = documentSnapshots.get(1);
                            DocumentSnapshot third = documentSnapshots.get(2);

                            userNameFirst.setText(first.getString("userName"));
                            userPointsFirst.setText("النقاط : " + first.getLong("userPoints"));

                            userNameSecond.setText(second.getString("userName"));
                            userPointsSecond.setText("النقاط : " + second.getLong("userPoints"));

                            userNameThird.setText(third.getString("userName"));
                            userPointsThird.setText("النقاط : " + third.getLong("userPoints"));
                        }
                        else{

                        }
                    }
                });
    }


    public void goBackToMainActivity(View view) {
        this.finish();
    }
}