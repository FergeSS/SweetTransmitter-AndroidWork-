package com.company.transmitter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.core.splashscreen.SplashScreen;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int page;
    static boolean isBack = true;
    static String favsInfo;
    ImageButton buttonMenu;
    ImageButton buttonBasket;
    ImageButton buttonMenuSlide;
    ImageButton buttonFav;
    ImageButton buttonCakes;
    ImageButton previousButton;
    ImageButton buttonCupcakes;
    ImageButton buttonMuffins;
    ImageButton buttonCheesecake;
    ImageButton dialogueExit;
    ImageButton dialogueStay;
    static ImageView dark;
    private List<Item> itemFav;
    private List<ItemBasket> itemsBasket;
    private ItemAdapter itemAdapter;
    private ItemAdapterBasket itemAdapterBasket;
    ViewGroup parentLayout;
    View menu;
    View basket;
    View fav;
    View previousView;
    static ConstraintLayout menuDelete;
    static TextView total;
    static TextView counterBasket;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 31){
            SplashScreen.installSplashScreen(this);
        }
        else {
            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        parentLayout = findViewById(R.id.mainLayout);
        LayoutInflater inflater = getLayoutInflater();
        menu = inflater.inflate(R.layout.menu, parentLayout, false);
        basket = inflater.inflate(R.layout.basket, parentLayout, false);
        fav = inflater.inflate(R.layout.fav, parentLayout, false);
        parentLayout.addView(menu);
        fullScreen();

        findElemntsById();
        setListeners();
        countOfOrders();

        pager = menu.findViewById(R.id.items);
        PagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                buttonCupcakes.setBackgroundResource(R.drawable.cupcakes);
                buttonMuffins.setBackgroundResource(R.drawable.muffins);
                buttonCakes.setBackgroundResource(R.drawable.cakes);
                buttonCheesecake.setBackgroundResource(R.drawable.cheesecake);

                if (position == 0) {
                    buttonCakes.setBackgroundResource(R.drawable.cakes_pressed);
                }
                else if (position == 1) {
                    buttonCupcakes.setBackgroundResource(R.drawable.cupcakes_pressed);
                }
                else if (position == 2) {
                    buttonMuffins.setBackgroundResource(R.drawable.muffins_pressed);
                }
                else if (position == 3) {
                    buttonCheesecake.setBackgroundResource(R.drawable.cheesecake_pressed);
                }


            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        previousView = menu;

    }
    private void findElemntsById(){
        buttonMenu = findViewById(R.id.buttonMenu);
        buttonBasket = findViewById(R.id.buttonBasket);
        buttonFav = findViewById(R.id.buttonFav);
        buttonCakes = findViewById(R.id.buttonCakes);
        buttonCupcakes = findViewById(R.id.buttonCupcakes);
        buttonMuffins = findViewById(R.id.buttonMuffins);
        buttonCheesecake = findViewById(R.id.buttonCheesecake);
        buttonMenuSlide = findViewById(R.id.buttonMenuSlide);
        dark = findViewById(R.id.darkening);
        counterBasket = findViewById(R.id.counterBasket);
        dark.setZ(-1);

    }
    private void setListeners() {

        buttonMenu.setOnClickListener(this);
        buttonBasket.setOnClickListener(this);
        buttonFav.setOnClickListener(this);
        buttonCakes.setOnClickListener(this);
        buttonCupcakes.setOnClickListener(this);
        buttonMuffins.setOnClickListener(this);
        buttonCheesecake.setOnClickListener(this);
        buttonMenuSlide.setOnClickListener(this);
    }
    private void fullScreen(){
        View activityRootView = findViewById(R.id.mainLayout);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = activityRootView.getRootView().getHeight();

                EditText ed1 = findViewById(R.id.comments);
                EditText ed2 = findViewById(R.id.name);

                // Вычисляем высоту клавиатуры
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight < screenHeight * 0.15) {
                    if (ed1 != null && ed1.hasFocus()) {
                        activityRootView.animate().translationY(0);
                    }
                    else if (ed2 != null && ed2.hasFocus()) {
                        activityRootView.animate().translationY(0);
                    }
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                } else {
                    int diff = 0;
                    float tmp = 50;
                    if (findViewById(R.id.edits) != null) {
                        tmp += findViewById(R.id.edits).getY();
                    }

                     if (ed1 != null && ed1.hasFocus() && ed1.getY() + ed1.getHeight() - diff + tmp > r.bottom ) {
                        activityRootView.animate().translationY(-(ed1.getY() + ed1.getHeight() + tmp - diff - r.bottom));
                      }
                    else if (ed2 != null && ed2.hasFocus() && ed2.getY() + ed2.getHeight() - diff + tmp > r.bottom ) {
                         activityRootView.animate().translationY(-(ed2.getY() + ed2.getHeight() + tmp - diff - r.bottom));
                     }
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        });


    }
    private void countOfOrders(){
        SharedPreferences shp = getSharedPreferences("basket", MODE_PRIVATE);
        String backetValue = shp.getString("basket", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
        String[] stringArray = backetValue.split(",");
        int[] basketValueInt = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            basketValueInt[i] = Integer.parseInt(stringArray[i]);
        }

        counterBasket.setText(Arrays.stream(basketValueInt).sum()+"");
        buttonCakes.setBackgroundResource(R.drawable.cakes_pressed);
    }
    private void setSlideMenu() {

        Dialog dialog = new Dialog(MainActivity.this);


        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.setContentView(R.layout.slide_menu);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);

            WindowManager.LayoutParams wlp = window.getAttributes();
             wlp.gravity = Gravity.BOTTOM | Gravity.START;
             wlp.dimAmount = 0.7f;

            window.setAttributes(wlp);



        }
        ImageButton menuSlideButtonMenu = dialog.findViewById(R.id.slideMenuButtonMenu);
        ImageButton menuSlideButtonHistory = dialog.findViewById(R.id.slideMenuButtonHistory);
        ImageButton menuSlideButtonPolicy = dialog.findViewById(R.id.slideMenuButtonPolicy);
        ImageButton menuSlideButtonRating = dialog.findViewById(R.id.slideMenuButtonRating);
        ImageButton menuSlideButtonAbout = dialog.findViewById(R.id.slideMenuButtonAbout);
        ImageButton menuSlideButtonExit = dialog.findViewById(R.id.slideMenuButtonExit);
        dialogueExit = dialog.findViewById(R.id.dialogueExit);
        dialogueStay = dialog.findViewById(R.id.dialogueStay);
        menuSlideButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogueExit.setVisibility(View.INVISIBLE);
                dialogueStay.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        });
        menuSlideButtonHistory.setOnClickListener(this);
        menuSlideButtonPolicy.setOnClickListener(this);
        menuSlideButtonRating.setOnClickListener(this);
        menuSlideButtonAbout.setOnClickListener(this);
        menuSlideButtonExit.setOnClickListener(this);
        dialogueStay.setOnClickListener(this);
        dialogueExit.setOnClickListener(this);
        dialog.show();

    }
    private void setMenu() {
        PagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(page);
        buttonMenuSlide = findViewById(R.id.buttonMenuSlide);
        parentLayout.removeView(previousView);
        buttonMenu.setBackgroundResource(R.drawable.menu);
        buttonBasket.setBackgroundResource(R.drawable.basket);
        buttonFav.setBackgroundResource(R.drawable.fav);
        counterBasket.setTextColor(getResources().getColor(R.color.black));

        buttonMenu.setBackgroundResource(R.drawable.menu_pressed);
        parentLayout.addView(menu);
        previousView = menu;
    }
    private void clearBasket() {
        SharedPreferences shp = getSharedPreferences("basket", MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putString("basket", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
        editor.apply();
        counterBasket.setText("0");
        total.setText("Total: 0");

    }
    private void addOrder() {
        SharedPreferences shp = getSharedPreferences("order", MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        List<ItemHistory> list = ItemHistory.parser(shp.getString("order", ""));

        String currDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String currTime = new SimpleDateFormat("HH:mm").format(new Date());

        SharedPreferences shp2 = getSharedPreferences("basket", MODE_PRIVATE);
        String backetValue = shp2.getString("basket", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
        String[] stringArray = backetValue.split(",");
        int[] basketValueInt = new int[stringArray.length];
        int[] images = {R.drawable.cake_1, R.drawable.cake_2,R.drawable.cake_3,R.drawable.cake_4,
                R.drawable.capcake_1,R.drawable.capcake_2,R.drawable.capcake_3,R.drawable.capcake_4,
                R.drawable.muffin_1, R.drawable.muffin_3,R.drawable.muffin_3,R.drawable.muffin_4,
                R.drawable.cheesecake_1, R.drawable.cheesecake_2, R.drawable.cheesecake_3, R.drawable.cheesecake_4
        };
        int image = 0;
        for (int i = 0; i < stringArray.length; i++) {
            basketValueInt[i] = Integer.parseInt(stringArray[i]);
            if (basketValueInt[i] != 0) {
                image = images[i];
                break;
            }
        }



        list.add(new ItemHistory(image, total.getText().toString(), currDate + "  " + currTime));
        String infoOrder = ItemHistory.toString(list);
        editor.putString("order", infoOrder);
        editor.apply();
    }
    private void clearPrevView() {
        parentLayout.removeView(previousView);
        buttonMenu.setBackgroundResource(R.drawable.menu);
        buttonBasket.setBackgroundResource(R.drawable.basket);
        buttonFav.setBackgroundResource(R.drawable.fav);

    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.darkening) {
            return;
        }
        if (view.getId() == R.id.buttonMenu) {
            if (previousView != menu) {
                setMenu();
            }
        }
        else if (view.getId() == R.id.buttonBasket) {
            if (previousView != basket) {
                clearPrevView();
                page = pager.getCurrentItem();

                parentLayout.addView(basket);

                buttonBasket.setBackgroundResource(R.drawable.basket__1_);
                counterBasket.setTextColor(getResources().getColor(R.color.white));

                previousView = basket;
                dark = findViewById(R.id.darkening);
                menuDelete = findViewById(R.id.menuDelete);
                menuDelete.setZ(2000);
                total = findViewById(R.id.total);

                RecyclerView recyclerViewBasket = findViewById(R.id.itemsBasket);
                recyclerViewBasket.setLayoutManager(new LinearLayoutManager(this));
                buttonMenuSlide = findViewById(R.id.buttonMenuSlide);
                buttonMenuSlide.setOnClickListener(this);

                itemsBasket = new ArrayList<>();

                fillListBasket(itemsBasket);
                ListIterator<ItemBasket> listIterator = itemsBasket.listIterator();
                while (listIterator.hasNext()) {
                    ItemBasket element = listIterator.next();
                    if (element.getCount() == 0) {
                        listIterator.remove(); // Удаление элемента во время итерации
                    }
                }
                itemAdapterBasket = new ItemAdapterBasket(itemsBasket, this);
                recyclerViewBasket.setAdapter(itemAdapterBasket);


                findViewById(R.id.buttonCheckout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOrderMenu(MainActivity.this);
                    }
                });

            }

        }
        else if (view.getId() == R.id.buttonFav) {
            if (previousView != fav) {
                clearPrevView();
                page = pager.getCurrentItem();

                buttonFav.setBackgroundResource(R.drawable.fav_pressed);
                parentLayout.addView(fav);
                RecyclerView recyclerViewFav = (RecyclerView) findViewById(R.id.itemsFav);
                recyclerViewFav.setLayoutManager(new LinearLayoutManager(this));

                previousView = fav;
                buttonMenuSlide = findViewById(R.id.buttonMenuSlide);
                buttonMenuSlide.setOnClickListener(this);
                counterBasket.setTextColor(getResources().getColor(R.color.black));

                itemFav = new ArrayList<>();

                fillListCakes(itemFav);
                fillListCupcakes(itemFav);
                fillListMuffins(itemFav);
                fillListChessecakes(itemFav);
                ListIterator<Item> listIterator = itemFav.listIterator();
                while (listIterator.hasNext()) {
                    Item element = listIterator.next();
                    if (element.getFav() == false) {
                        listIterator.remove(); // Удаление элемента во время итерации
                    }
                }
                itemAdapter = new ItemAdapter(itemFav, this, true);
                recyclerViewFav.setAdapter(itemAdapter);
            }
        }
        else if (view.getId() == R.id.buttonCakes) {
            if (previousButton != buttonCakes) {
                pager.setCurrentItem(0);
            }
        }
        else if (view.getId() == R.id.buttonCupcakes) {
            if (previousButton != buttonCupcakes) {
                pager.setCurrentItem(1);
            }
        }
        else if (view.getId() == R.id.buttonMuffins) {
            if (previousButton != buttonMuffins) {
                pager.setCurrentItem(2);
            }
        }
        else if (view.getId() == R.id.buttonCheesecake) {
            if (previousButton != buttonCheesecake) {
                pager.setCurrentItem(3);
            }
        }
        else if (view.getId() == R.id.buttonMenuSlide) {
            setSlideMenu();
        }

        else if (view.getId() == R.id.slideMenuButtonAbout) {
            page = pager.getCurrentItem();
            dialogueExit.setVisibility(View.INVISIBLE);
            dialogueStay.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(this, AboutUs.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.slideMenuButtonHistory) {
            page = pager.getCurrentItem();
            dialogueExit.setVisibility(View.INVISIBLE);
            dialogueStay.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(this, HistoryOfOrders.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.slideMenuButtonPolicy) {
            page = pager.getCurrentItem();
            dialogueExit.setVisibility(View.INVISIBLE);
            dialogueStay.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(this, PolicyPrivacy.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.slideMenuButtonRating) {
            page = pager.getCurrentItem();
            dialogueExit.setVisibility(View.INVISIBLE);
            dialogueStay.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
            startActivity(intent);
        }
        else if (view.getId() == R.id.slideMenuButtonExit) {
            page = pager.getCurrentItem();
            dialogueExit.setVisibility(View.VISIBLE);
            dialogueStay.setVisibility(View.VISIBLE);
        }
        else if(view.getId() == R.id.dialogueStay) {
            page = pager.getCurrentItem();
            dialogueExit.setVisibility(View.INVISIBLE);
            dialogueStay.setVisibility(View.INVISIBLE);
        }
        else if(view.getId() == R.id.dialogueExit) {
            finishAffinity();
        }
    }
    private void fillListBasket(List<ItemBasket> list) {
        SharedPreferences shp = getSharedPreferences("basket", MODE_PRIVATE);
        String backetValue = shp.getString("basket", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
        String[] stringArray = backetValue.split(",");
        int[] basketValueInt = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            basketValueInt[i] = Integer.parseInt(stringArray[i]);
        }
        list.add(new ItemBasket(R.drawable.cake_1, "Chocolate Delight", 45, 0, basketValueInt[0]));
        list.add(new ItemBasket(R.drawable.cake_2, "Fruit Paradise", 55, 1, basketValueInt[1]));
        list.add(new ItemBasket(R.drawable.cake_3, "Honeymoon", 60, 2, basketValueInt[2]));
        list.add(new ItemBasket(R.drawable.cake_4, "Red Velvet", 52,  3, basketValueInt[3]));
        list.add(new ItemBasket(R.drawable.capcake_1, "Vanilla cupcakes with chocolate frosting", 38, 4, basketValueInt[4]));
        list.add(new ItemBasket(R.drawable.capcake_2, "Red Velvet Cupcakes", 35, 5, basketValueInt[5]));
        list.add(new ItemBasket(R.drawable.capcake_3, "Carrot cupcakes", 34, 6, basketValueInt[6]));
        list.add(new ItemBasket(R.drawable.capcake_4, "Lemon cupcakes", 38, 7, basketValueInt[7]));
        list.add(new ItemBasket(R.drawable.muffin_1, "Chocolate muffins with cherries", 33, 8, basketValueInt[8]));
        list.add(new ItemBasket(R.drawable.muffin_2, "Banana and chocolate muffins", 35, 9, basketValueInt[9]));
        list.add(new ItemBasket(R.drawable.muffin_3, "Lemon muffins", 35, 10, basketValueInt[10]));
        list.add(new ItemBasket(R.drawable.muffin_4, "Vanilla muffins with berries", 40, 11, basketValueInt[11]));
        list.add(new ItemBasket(R.drawable.cheesecake_1, "Classic cheesecake", 60, 12, basketValueInt[12]));
        list.add(new ItemBasket(R.drawable.cheesecake_2, "New York cheesecake", 65, 13, basketValueInt[13]));
        list.add(new ItemBasket(R.drawable.cheesecake_3, "Chocolate cheesecake", 62, 14, basketValueInt[14]));
        list.add(new ItemBasket(R.drawable.cheesecake_4, "Caramel cheesecake", 63, 15, basketValueInt[15]));
    }
    private void fillListCakes(List<Item> list){
        favsInfo = getSharedPreferences("FAV", MODE_PRIVATE).getString("FAV", "0000000000000000");
        list.add(new Item(R.drawable.cake_1, "Chocolate Delight", 45, favsInfo.charAt(0) == '1' ? true : false, 0, R.drawable.big_1, "This is a classic chocolate cake, which consists of several layers of chocolate sponge cake, soaked in chocolate cream and decorated with chocolate glaze. The cake has a rich chocolate taste and delicate texture. It's perfect for chocolate lovers."));
        list.add(new Item(R.drawable.cake_2, "Fruit Paradise", 55, favsInfo.charAt(1) == '1' ? true : false, 1, R.drawable.big_2, "This cake is a combination of a delicate sponge cake with fruit filling and butter cream. The filling uses fresh or canned fruits such as strawberries, raspberries, peaches or pineapples. Fruits give the cake a fresh and bright taste, and the cream makes it soft and airy."));
        list.add(new Item(R.drawable.cake_3, "Honeymoon", 60, favsInfo.charAt(2) == '1' ? true : false, 2, R.drawable.big_3, "“Honeymoon” cake is a combination of honey cakes with delicate sour cream. The cakes have a rich honey taste and aroma, and the cream gives the cake a slight sourness. This cake is perfect for a romantic dinner or celebration."));
        list.add(new Item(R.drawable.cake_4, "Red Velvet", 52, favsInfo.charAt(3) == '1' ? true : false, 3, R.drawable.big_4, "Red velvet is a popular American cake consisting of a moist and spongy red cake layer that contrasts with a snow-white cream cheese frosting. The taste of the cake is very delicate: it has berry notes, soft velvety, and pleasant sweetness."));
    }
    private void fillListCupcakes(List<Item> list){
        favsInfo = getSharedPreferences("FAV", MODE_PRIVATE).getString("FAV", "0000000000000000");
        list.add(new Item(R.drawable.capcake_1, "Vanilla cupcakes with chocolate frosting", 38, favsInfo.charAt(4) == '1' ? true : false, 4, R.drawable.big_5, "These cupcakes have a creamy vanilla base and are topped with rich chocolate frosting. They are ideal for lovers of classic taste."));
        list.add(new Item(R.drawable.capcake_2, "Red Velvet Cupcakes", 35, favsInfo.charAt(5) == '1' ? true : false, 5, R.drawable.big_6, "Red velvet cupcakes have a rich red color and a delicate flavor. Their base consists of a mixture of butter, sugar, eggs and flour, as well as a small amount of cocoa powder and food coloring. These cupcakes are decorated with buttercream and sprinkled with chocolate chips."));
        list.add(new Item(R.drawable.capcake_3, "Carrot cupcakes", 34, favsInfo.charAt(6) == '1' ? true : false, 6, R.drawable.big_7, "Carrot cupcakes are a great choice for those who love unusual flavors. They are based on carrots, which gives them a bright orange color and a sweetish taste. These cupcakes are usually decorated with butter or cheese frosting and may be topped with nuts or candied fruits."));
        list.add(new Item(R.drawable.capcake_4, "Lemon cupcakes", 38, favsInfo.charAt(7) == '1' ? true : false, 7, R.drawable.big_8, "Lemon cupcakes have a refreshing citrus flavor. Their base is made from butter, sugar and eggs, and then lemon juice and zest are added. These cupcakes can be decorated with lemon glaze or sprinkled with powdered sugar."));
    }
    private void fillListChessecakes(List<Item> list){
        favsInfo = getSharedPreferences("FAV", MODE_PRIVATE).getString("FAV", "0000000000000000");
        list.add(new Item(R.drawable.cheesecake_1, "Classic cheesecake", 60, favsInfo.charAt(12) == '1' ? true : false, 12, R.drawable.big_13, "This is the most popular type of cheesecake, which consists of a shortcrust pastry base and cream cheese filling. The filling has a creamy texture and delicate taste. The cheesecake is baked in the oven until golden brown."));
        list.add(new Item(R.drawable.cheesecake_2, "New York cheesecake", 65, favsInfo.charAt(13) == '1' ? true : false, 13, R.drawable.big_14, "This type of cheesecake differs from the classic one in that it has a denser and richer filling. The filling includes cream cheese, eggs and sugar. Cheesecake is also baked in the oven, but longer than the classic one."));
        list.add(new Item(R.drawable.cheesecake_3, "Chocolate cheesecake", 62, favsInfo.charAt(14) == '1' ? true : false, 14, R.drawable.big_15, "Chocolate is added to this type of cheesecake, which gives it a rich taste and aroma. Chocolate can be added to the shortcrust pastry base or cream cheese filling. The cheesecake can be decorated with chocolate glaze or sprinkled with chocolate chips."));
        list.add(new Item(R.drawable.cheesecake_4, "Caramel cheesecake", 63, favsInfo.charAt(15) == '1' ? true : false, 15, R.drawable.big_16, "Cheesecake with caramel taste and aroma. To prepare it, caramel is used, which is added to the cream cheese filling or as a topping. The cheesecake is very sweet and rich."));
    }
    private void fillListMuffins(List<Item> list){
        favsInfo = getSharedPreferences("FAV", MODE_PRIVATE).getString("FAV", "0000000000000000");
        list.add(new Item(R.drawable.muffin_1, "Chocolate muffins with cherries", 33, favsInfo.charAt(8) == '1' ? true : false, 8, R.drawable.big_9, "These muffins have a rich chocolate taste and aroma, as well as a delicate texture. They are perfect for chocolate lovers. Cherries are used as a filling, which gives the muffins a sweet and sour taste."));
        list.add(new Item(R.drawable.muffin_2, "Banana and chocolate muffins", 35, favsInfo.charAt(9) == '1' ? true : false, 9, R.drawable.big_10, "Muffins filled with banana and chocolate are a combination of delicate banana flavor and rich chocolate flavor. The banana adds sweetness and moisture to the muffin, while the chocolate adds aroma and flavor. These muffins are perfect for breakfast or snack."));
        list.add(new Item(R.drawable.muffin_3, "Lemon muffins", 35, favsInfo.charAt(10) == '1' ? true : false, 10, R.drawable.big_11, "Lemon muffins are a classic American dessert. They have a delicate lemon taste and aroma. Lemon zest gives the muffins a pleasant bitterness, and lemon juice adds sourness. These muffins go great with tea or coffee."));
        list.add(new Item(R.drawable.muffin_4, "Vanilla muffins with berries", 40, favsInfo.charAt(11) == '1' ? true : false, 11, R.drawable.big_12, "Vanilla muffins with berries are a tender and airy treat. Berries give the muffins freshness and juiciness, and vanilla gives a pleasant aroma. These muffins are an excellent choice for tea or dessert after lunch."));
    }
    @Override
    public void onBackPressed() {
        if(isBack) {
            finish();
        }

    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        page = pager.getCurrentItem();
    }

    public void showOrderMenu(Context context){

        Dialog dialog = new Dialog(context);


        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.order_menu);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);

            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            wlp.dimAmount = 0.7f;

            window.setAttributes(wlp);

        }
        isBack = false;
        String totalString = total.getText().toString();



        if (totalString.charAt(totalString.length() - 1) == '0' && totalString.charAt(totalString.length() - 2) == ' ') { return; }

        TextView totalCard = dialog.findViewById(R.id.cardTotal);
        totalCard.setText(totalString);


       dialog.findViewById(R.id.cardButtonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                MainActivity.isBack = true;
                dialog.dismiss();
            }
        });

        RadioButton radioButton1 = dialog.findViewById(R.id.cash);
        RadioButton radioButton2 = dialog.findViewById(R.id.cardTransfer);
        RadioButton radioButton3 = dialog.findViewById(R.id.card);
        RadioButton[] radioButtons = {radioButton1, radioButton2, radioButton3};

        for (RadioButton radioButton : radioButtons) {
            radioButton.setOnClickListener(view -> {
                 for (RadioButton rb : radioButtons) {
                    rb.setChecked(false);
                }
                 radioButton.setChecked(true);
            });
        }
        dialog.findViewById(R.id.cardContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!radioButton1.isChecked() && !radioButton2.isChecked() && !radioButton3.isChecked()) {
                    return;
                }
                int[] tmp = {R.id.adress, R.id.apartament, R.id.entrance, R.id.floor, R.id.name, R.id.comments};
                for (int i = 0; i < tmp.length; ++i) {
                    EditText tmp1 = dialog.findViewById(tmp[i]);
                    if (tmp1.getText().toString().length() == 0) {
                        return;
                    }
                }
                addOrder();
                clearBasket();


                dialog.setContentView(R.layout.order_thx_menu);

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        isBack = true;
                        dialog.dismiss();
                        setMenu();
                    }
                });



                dialog.findViewById(R.id.cardMenu).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isBack = true;
                        dialog.dismiss();
                        setMenu();

                    }
                });

            }
        });
        dialog.show();
    }


}