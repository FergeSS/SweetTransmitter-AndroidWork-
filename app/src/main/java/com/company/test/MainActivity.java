package com.company.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.splashscreen.SplashScreen;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String favsInfo;
    ImageButton buttonMenu;
    ImageButton buttonBasket;
    ImageButton buttonMenuSlide;
    ImageButton buttonFav;
    ImageButton buttonCakes;
    ImageButton previousButton;
    ImageButton buttonCupcakes;
    ImageButton buttonMuffins;
    ImageButton buttonCheesecake;
    ImageView menuFrame;
    ImageButton menuSlideButtonMenu;
    ImageButton menuSlideButtonHistory;
    ImageButton menuSlideButtonPolicy;
    ImageButton menuSlideButtonRating;
    ImageButton menuSlideButtonAbout;
    ImageButton menuSlideButtonExit;
    ImageButton dialogueExit;
    ImageButton dialogueStay;
    static ImageView dark;
    ConstraintLayout slideMenu;
    static ConstraintLayout card;
    private List<Item> items;
    private List<Item> itemFav;
    private List<ItemBasket> itemsBasket;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewBasket;
    private RecyclerView recyclerViewFav;
    private ItemAdapter itemAdapter;
    private ItemAdapterBasket itemAdapterBasket;
    ViewGroup parentLayout;
    View menu;
    View basket;
    View fav;
    View previousView;
    int idCurrentView = 1; // 0 - slide menu, 1 - cakes, 2 - cupcakes, 3 - muffins, 4 - cheesecakes
    boolean isSlide = false;
    Handler hand;
    Runnable runnable;
    static ConstraintLayout menuDelete;
    static TextView total;
    static TextView counterBasket;

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

        SharedPreferences shp = getSharedPreferences("FAV", MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        favsInfo = shp.getString("FAV", "0000000000000000");

        parentLayout = findViewById(R.id.mainLayout);

        LayoutInflater infalter = getLayoutInflater();
        menu = infalter.inflate(R.layout.menu, parentLayout, false);
        basket = infalter.inflate(R.layout.basket, parentLayout, false);
        fav = infalter.inflate(R.layout.fav, parentLayout, false);
        parentLayout.addView(menu);

        recyclerView = findViewById(R.id.items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        items = new ArrayList<>();
        fillListCakes(items);

        itemAdapter = new ItemAdapter(items, this, false);
        recyclerView.setAdapter(itemAdapter);

        buttonMenu = findViewById(R.id.buttonMenu);
        buttonBasket = findViewById(R.id.buttonBasket);
        buttonFav = findViewById(R.id.buttonFav);
        buttonCakes = findViewById(R.id.buttonCakes);
        buttonCupcakes = findViewById(R.id.buttonCupcakes);
        buttonMuffins = findViewById(R.id.buttonMuffins);
        buttonCheesecake = findViewById(R.id.buttonCheesecake);
        buttonMenuSlide = findViewById(R.id.buttonMenuSlide);
        menuFrame = findViewById(R.id.menuFrame);
        menuSlideButtonMenu = findViewById(R.id.slideMenuButtonMenu);
        menuSlideButtonHistory = findViewById(R.id.slideMenuButtonHistory);
        menuSlideButtonPolicy = findViewById(R.id.slideMenuButtonPolicy);
        menuSlideButtonRating = findViewById(R.id.slideMenuButtonRating);
        menuSlideButtonAbout = findViewById(R.id.slideMenuButtonAbout);
        menuSlideButtonExit = findViewById(R.id.slideMenuButtonExit);
        slideMenu = findViewById(R.id.menuLayout);
        dark = findViewById(R.id.darkening);
        dialogueExit = findViewById(R.id.dialogueExit);
        dialogueStay = findViewById(R.id.dialogueStay);
        card = findViewById(R.id.layoutCard);
        counterBasket = findViewById(R.id.counterBasket);

        buttonMenu.setOnClickListener(this);
        buttonBasket.setOnClickListener(this);
        buttonFav.setOnClickListener(this);
        buttonCakes.setOnClickListener(this);
        buttonCupcakes.setOnClickListener(this);
        buttonMuffins.setOnClickListener(this);
        buttonCheesecake.setOnClickListener(this);
        buttonMenuSlide.setOnClickListener(this);
        menuSlideButtonMenu.setOnClickListener(this);
        menuSlideButtonHistory.setOnClickListener(this);
        menuSlideButtonPolicy.setOnClickListener(this);
        menuSlideButtonRating.setOnClickListener(this);
        menuSlideButtonAbout.setOnClickListener(this);
        menuSlideButtonExit.setOnClickListener(this);
        dark.setOnClickListener(this);
        dialogueStay.setOnClickListener(this);
        dialogueExit.setOnClickListener(this);

        itemsBasket = new ArrayList<>();

        fillListBasket(itemsBasket);
        ListIterator<ItemBasket> listIterator = itemsBasket.listIterator();
        while (listIterator.hasNext()) {
            ItemBasket element = listIterator.next();
            if (element.getCount() == 0) {
                listIterator.remove(); // Удаление элемента во время итерации
            }
        }
        counterBasket.setText(itemsBasket.size()+"");
        itemsBasket.clear();


        dark.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeLeft() {
                destroySlideMenu();
            }
        });

        setSliderOnMenu();

        buttonCakes.setBackgroundResource(R.drawable.cakes_pressed);

        previousView = menu;
        previousButton = buttonCakes;
        slideMenu.setZ(1000);
        dark.setZ(-1);
    }

    private void setSliderOnMenu() {
        recyclerView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeLeft() {
                if (idCurrentView == 1) {
                    setCupcakes();
                }
                else if (idCurrentView == 2) {
                    setMuffins();

                }
                else if (idCurrentView == 3) {
                    setCheesecake();
                }
            }
            public void onSwipeRight() {
                if (idCurrentView == 1) {
                    setSlideMenu();

                }
                else if (idCurrentView == 2) {
                    setCakes();
                }
                else if (idCurrentView == 3) {
                    setCupcakes();
                }
                else if (idCurrentView == 4) {
                    setMuffins();
                }
            }
        });
    }
    private void setSlideMenu() {
        idCurrentView = 0;
        dark.setZ(999);
        dark.animate().alpha(0.7f).setDuration(500);
        slideMenu.animate().translationX(menuFrame.getWidth()).setDuration(500);
    }
    private void setCheesecake() {
        idCurrentView = 4;
        buttonCupcakes.setBackgroundResource(R.drawable.cupcakes);
        buttonMuffins.setBackgroundResource(R.drawable.muffins);
        buttonCakes.setBackgroundResource(R.drawable.cakes);

        buttonCheesecake.setBackgroundResource(R.drawable.cheesecake_pressed);

        previousButton = buttonCheesecake;
        items.clear();
        fillListChessecakes(items);
        itemAdapter = new ItemAdapter(items, this, false);
        recyclerView.setAdapter(itemAdapter);
    }
    private void setMuffins() {
        idCurrentView = 3;
        buttonCupcakes.setBackgroundResource(R.drawable.cupcakes);
        buttonCheesecake.setBackgroundResource(R.drawable.cheesecake);
        buttonCakes.setBackgroundResource(R.drawable.cakes);

        buttonMuffins.setBackgroundResource(R.drawable.muffins_pressed);

        previousButton = buttonMuffins;
        items.clear();
        fillListMuffins(items);
        itemAdapter = new ItemAdapter(items, this,  false);
        recyclerView.setAdapter(itemAdapter);
    }
    private void setCupcakes() {
        idCurrentView = 2;
        buttonMuffins.setBackgroundResource(R.drawable.muffins);
        buttonCheesecake.setBackgroundResource(R.drawable.cheesecake);
        buttonCakes.setBackgroundResource(R.drawable.cakes);

        buttonCupcakes.setBackgroundResource(R.drawable.cupcakes_pressed);

        previousButton = buttonCupcakes;
        items.clear();
        fillListCupcakes(items);
        itemAdapter = new ItemAdapter(items, this, false);
        recyclerView.setAdapter(itemAdapter);
    }
    private void setCakes() {
        idCurrentView = 1;
       buttonCupcakes.setBackgroundResource(R.drawable.cupcakes);
       buttonMuffins.setBackgroundResource(R.drawable.muffins);
       buttonCheesecake.setBackgroundResource(R.drawable.cheesecake);

       buttonCakes.setBackgroundResource(R.drawable.cakes_pressed);
       previousButton = buttonCakes;
       items.clear();
       fillListCakes(items);
       itemAdapter = new ItemAdapter(items, this,  false);
       recyclerView.setAdapter(itemAdapter);
   }
    private void destroySlideMenu() {
        dialogueExit.setVisibility(View.INVISIBLE);
        dialogueStay.setVisibility(View.INVISIBLE);
        if (previousButton == buttonCakes) {
            idCurrentView = 1;
        }
        else if (previousButton == buttonCupcakes) {
            idCurrentView = 2;
        }
        else if (previousButton == buttonMuffins) {
            idCurrentView = 3;
        }
        else if (previousButton == buttonCheesecake) {
            idCurrentView = 4;
        }
       dark.animate().alpha(0).setDuration(500);
       slideMenu.animate().translationX(-menuFrame.getWidth()).setDuration(500);
       hand = new Handler(Looper.getMainLooper());

       runnable = new Runnable() {
           @Override
           public void run() {
               dark.setZ(-1);
           }
       };

       hand.postDelayed(runnable, 500);
   }
   private void setMenu() {

       if (previousView != menu) {
           items.clear();
           if (previousButton == buttonCakes) {
               fillListCakes(items);
           }
           if (previousButton == buttonCheesecake) {
               fillListChessecakes(items);
           }
           if (previousButton == buttonCupcakes) {
               fillListCupcakes(items);
           }
           if (previousButton == buttonMuffins) {
               fillListMuffins(items);
           }
           buttonMenuSlide = findViewById(R.id.buttonMenuSlide);
           itemAdapter = new ItemAdapter(items, this, false);
           recyclerView.setAdapter(itemAdapter);
           parentLayout.removeView(previousView);
           buttonMenu.setBackgroundResource(R.drawable.menu);
           buttonBasket.setBackgroundResource(R.drawable.basket);
           buttonFav.setBackgroundResource(R.drawable.fav);
           counterBasket.setTextColor(getResources().getColor(R.color.black));

           buttonMenu.setBackgroundResource(R.drawable.menu_pressed);
           parentLayout.addView(menu);
           previousView = menu;
       }
   }
   private void clearBasket() {
       SharedPreferences shp = getSharedPreferences("basket", MODE_PRIVATE);
       SharedPreferences.Editor editor = shp.edit();
       editor.putString("basket", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
       editor.apply();
       counterBasket.setText("0");
       total.setText("Total: 0");

   }

   private void clearOrderFrame() {
       int[] tmp = {R.id.adress, R.id.apartament, R.id.entrance, R.id.floor, R.id.name, R.id.comments};
       for (int i = 0; i < tmp.length; ++i) {
           EditText tmp1 = findViewById(tmp[i]);
           tmp1.setText("");
       }
       RadioButton radioButton1 = findViewById(R.id.cash);
       RadioButton radioButton2 = findViewById(R.id.cardTransfer);
       RadioButton radioButton3 = findViewById(R.id.card);
       radioButton1.setChecked(false);
       radioButton2.setChecked(false);
       radioButton3.setChecked(false);
   }

   private void addOrder() {
       SharedPreferences shp = getSharedPreferences("order", MODE_PRIVATE);
       SharedPreferences.Editor editor = shp.edit();
       List<ItemHistory> list = ItemHistory.parser(shp.getString("order", ""));

       LocalDate currentDate = LocalDate.now();

       // Получаем текущее время
       LocalTime currentTime = LocalTime.now();

       // Форматируем дату и время в удобочитаемый вид
       DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
       DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

       String formattedDate = currentDate.format(dateFormatter);
       String formattedTime = currentTime.format(timeFormatter);

       int[] images = {R.drawable.cake_1, R.drawable.cake_2,R.drawable.cake_3,R.drawable.cake_4,
               R.drawable.capcake_1,R.drawable.capcake_2,R.drawable.capcake_3,R.drawable.capcake_4,
               R.drawable.cheesecake_1, R.drawable.cheesecake_2, R.drawable.cheesecake_3, R.drawable.cheesecake_4,
               R.drawable.muffin_1, R.drawable.muffin_3,R.drawable.muffin_3,R.drawable.muffin_4};
       Random rand = new Random();
       list.add(new ItemHistory(images[rand.nextInt(16)], total.getText().toString(), formattedDate + "  " + formattedTime));
       String infoOrder = ItemHistory.toString(list);
       editor.putString("order", infoOrder);
       editor.apply();
   }
   @Override
    public void onClick(View view) {
        if (view.getId() == R.id.darkening) {
            return;
        }
       if (view.getId() == R.id.buttonMenu) {
           setMenu();
       }
       else if (view.getId() == R.id.buttonBasket) {
            if (previousView != basket) {
                parentLayout.removeView(previousView);
                buttonMenu.setBackgroundResource(R.drawable.menu);
                buttonBasket.setBackgroundResource(R.drawable.basket);
                buttonFav.setBackgroundResource(R.drawable.fav);

                parentLayout.addView(basket);
                buttonBasket.setBackgroundResource(R.drawable.basket__1_);
                counterBasket.setTextColor(getResources().getColor(R.color.white));

                previousView = basket;
                dark = findViewById(R.id.darkening);
                menuDelete = findViewById(R.id.menuDelete);
                menuDelete.setZ(2000);
                total = findViewById(R.id.total);

                recyclerViewBasket = findViewById(R.id.itemsBasket);
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
                        String totalString = total.getText().toString();

                        if (totalString.charAt(totalString.length() - 1) == '0' && totalString.charAt(totalString.length() - 2) == ' ') { return; }
                        ConstraintLayout layoutOrder = findViewById(R.id.layoutOrder);
                        ImageView dark2 = findViewById(R.id.darkeningBasket);
                        TextView totalCard = layoutOrder.findViewById(R.id.cardTotal);
                        totalCard.setText(totalString);
                        layoutOrder.animate().translationY(-layoutOrder.getHeight()).setDuration(300);
                        dark2.setZ(999);
                        layoutOrder.setZ(1000);
                        dark2.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                        dark2.animate().alpha(0.7f).setDuration(300);

                        layoutOrder.findViewById(R.id.cardButtonClose).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                layoutOrder.animate().translationY(layoutOrder.getHeight()).setDuration(300);
                                layoutOrder.setZ(-1);
                                dark2.animate().alpha(0.0f).setDuration(300);
                                Handler hand = new Handler(Looper.getMainLooper());

                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        dark2.setZ(-1);
                                    }
                                };
                                hand.postDelayed(runnable, 300);
                            }
                        });



                        RadioButton radioButton1 = findViewById(R.id.cash);
                        RadioButton radioButton2 = findViewById(R.id.cardTransfer);
                        RadioButton radioButton3 = findViewById(R.id.card);

                        // Создайте массив радиокнопок
                        RadioButton[] radioButtons = {radioButton1, radioButton2, radioButton3};

                        // Настройка OnClickListener для каждой радиокнопки
                        for (RadioButton radioButton : radioButtons) {
                            radioButton.setOnClickListener(view -> {
                                // Снимите отметку со всех радиокнопок
                                for (RadioButton rb : radioButtons) {
                                    rb.setChecked(false);
                                }
                                // Установите отметку на нажатую радиокнопку
                                radioButton.setChecked(true);

                                // Показать сообщение с выбранной опцией
                                Toast.makeText(MainActivity.this, "Selected: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
                            });
                        }

                        layoutOrder.findViewById(R.id.cardContinue).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!radioButton1.isChecked() && !radioButton2.isChecked() && !radioButton3.isChecked()) { return; }
                                int[] tmp = {R.id.adress, R.id.apartament, R.id.entrance, R.id.floor, R.id.name, R.id.comments};
                                for (int i = 0; i < tmp.length; ++i) {
                                    EditText tmp1 = layoutOrder.findViewById(tmp[i]);
                                    if (tmp1.getText().toString().length() == 0) {
                                        return;
                                    }
                                }
                                addOrder();
                                clearBasket();
                                clearOrderFrame();
                                ConstraintLayout layoutOrderThx = findViewById(R.id.layoutOrderThx);
                                layoutOrderThx.animate().translationY(-layoutOrder.getHeight()).setDuration(300);
                                layoutOrderThx.setZ(1000);
                                layoutOrder.animate().translationY(layoutOrder.getHeight()).setDuration(300);
                                layoutOrder.setZ(-1);
                                layoutOrderThx.findViewById(R.id.cardMenu).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        layoutOrderThx.animate().translationY(layoutOrder.getHeight()).setDuration(300);
                                        layoutOrderThx.setZ(-1);
                                        dark2.animate().alpha(0.0f).setDuration(300);
                                        Handler hand = new Handler(Looper.getMainLooper());

                                        Runnable runnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                dark2.setZ(-1);
                                                dark2.setOnTouchListener(new View.OnTouchListener() {
                                                    @Override
                                                    public boolean onTouch(View v, MotionEvent event) {
                                                        return false;
                                                    }
                                                });
                                            }
                                        };
                                        hand.postDelayed(runnable, 300);
                                        setMenu();
                                    }
                                });

                            }
                        });
                    }
                });

            }

       }
       else if (view.getId() == R.id.buttonFav) {
            if (previousView != fav) {

                parentLayout.removeView(previousView);
                buttonMenu.setBackgroundResource(R.drawable.menu);
                buttonBasket.setBackgroundResource(R.drawable.basket);
                buttonFav.setBackgroundResource(R.drawable.fav);

                buttonFav.setBackgroundResource(R.drawable.fav_pressed);
                parentLayout.addView(fav);
                recyclerViewFav = (RecyclerView) findViewById(R.id.itemsFav);
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
                setCakes();
            }
       }
       else if (view.getId() == R.id.buttonCupcakes) {
            if (previousButton != buttonCupcakes) {
               setCupcakes();
            }
       }
       else if (view.getId() == R.id.buttonMuffins) {
            if (previousButton != buttonMuffins) {
               setMuffins();
            }
       }
       else if (view.getId() == R.id.buttonCheesecake) {
            if (previousButton != buttonCheesecake) {
                setCheesecake();
            }
       }

       else if (view.getId() == R.id.buttonMenuSlide) {
            setSlideMenu();
       }
       else if (view.getId() == R.id.slideMenuButtonMenu){
            destroySlideMenu();
       }
       else if (view.getId() == R.id.slideMenuButtonAbout) {
           dialogueExit.setVisibility(View.INVISIBLE);
           dialogueStay.setVisibility(View.INVISIBLE);
           Intent intent = new Intent(this, AboutUs.class);
           startActivity(intent);
       }
       else if (view.getId() == R.id.slideMenuButtonHistory) {
           dialogueExit.setVisibility(View.INVISIBLE);
           dialogueStay.setVisibility(View.INVISIBLE);
           Intent intent = new Intent(this, HistoryOfOrders.class);
           startActivity(intent);
       }
       else if (view.getId() == R.id.slideMenuButtonPolicy) {
           dialogueExit.setVisibility(View.INVISIBLE);
           dialogueStay.setVisibility(View.INVISIBLE);
           Intent intent = new Intent(this, PolicyPrivacy.class);
           startActivity(intent);
       }
       else if (view.getId() == R.id.slideMenuButtonRating) {
           dialogueExit.setVisibility(View.INVISIBLE);
           dialogueStay.setVisibility(View.INVISIBLE);
           //to do
       }
       else if (view.getId() == R.id.slideMenuButtonExit) {
           dialogueExit.setVisibility(View.VISIBLE);
           dialogueStay.setVisibility(View.VISIBLE);
       }
       else if(view.getId() == R.id.dialogueStay) {
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
        list.add(new ItemBasket(R.drawable.capcake_1, "Vanilla cupcakes\nwith chocolate frosting", 38, 4, basketValueInt[4]));
        list.add(new ItemBasket(R.drawable.capcake_2, "Red Velvet Cupcakes", 35, 5, basketValueInt[5]));
        list.add(new ItemBasket(R.drawable.capcake_3, "Carrot cupcakes", 34, 6, basketValueInt[6]));
        list.add(new ItemBasket(R.drawable.capcake_4, "Lemon cupcakes", 38, 7, basketValueInt[7]));
        list.add(new ItemBasket(R.drawable.muffin_1, "Chocolate muffins with cherries", 60, 8, basketValueInt[8]));
        list.add(new ItemBasket(R.drawable.muffin_2, "Banana and chocolate muffins", 65, 9, basketValueInt[9]));
        list.add(new ItemBasket(R.drawable.muffin_3, "Lemon muffins", 62, 10, basketValueInt[10]));
        list.add(new ItemBasket(R.drawable.muffin_4, "Vanilla muffins with berries", 63, 11, basketValueInt[11]));
        list.add(new ItemBasket(R.drawable.cheesecake_1, "Classic cheesecake", 33, 12, basketValueInt[12]));
        list.add(new ItemBasket(R.drawable.cheesecake_2, "New York cheesecake", 35, 13, basketValueInt[13]));
        list.add(new ItemBasket(R.drawable.cheesecake_3, "Chocolate cheesecake", 35, 14, basketValueInt[14]));
        list.add(new ItemBasket(R.drawable.cheesecake_4, "Caramel cheesecake", 40, 15, basketValueInt[15]));
    }

    private void fillListCakes(List<Item> list){
        SharedPreferences shp = getSharedPreferences("FAV", MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        favsInfo = shp.getString("FAV", "0000000000000000");
        list.add(new Item(R.drawable.cake_1, "Chocolate Delight", 45, favsInfo.charAt(0) == '1' ? true : false, 0, R.drawable.big_1, "This is a classic chocolate cake, which consists of several layers of chocolate sponge cake, soaked in chocolate cream and decorated with chocolate glaze. The cake has a rich chocolate taste and delicate texture. It's perfect for chocolate lovers."));
        list.add(new Item(R.drawable.cake_2, "Fruit Paradise", 55, favsInfo.charAt(1) == '1' ? true : false, 1, R.drawable.big_2, "This cake is a combination of a delicate sponge cake with fruit filling and butter cream. The filling uses fresh or canned fruits such as strawberries, raspberries, peaches or pineapples. Fruits give the cake a fresh and bright taste, and the cream makes it soft and airy."));
        list.add(new Item(R.drawable.cake_3, "Honeymoon", 60, favsInfo.charAt(2) == '1' ? true : false, 2, R.drawable.big_3, "“Honeymoon” cake is a combination of honey cakes with delicate sour cream. The cakes have a rich honey taste and aroma, and the cream gives the cake a slight sourness. This cake is perfect for a romantic dinner or celebration."));
        list.add(new Item(R.drawable.cake_4, "Red Velvet", 52, favsInfo.charAt(3) == '1' ? true : false, 3, R.drawable.big_4, "Red velvet is a popular American cake consisting of a moist and spongy red cake layer that contrasts with a snow-white cream cheese frosting. The taste of the cake is very delicate: it has berry notes, soft velvety, and pleasant sweetness."));
    }

    private void fillListCupcakes(List<Item> list){
        SharedPreferences shp = getSharedPreferences("FAV", MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        favsInfo = shp.getString("FAV", "0000000000000000");
        list.add(new Item(R.drawable.capcake_1, "Vanilla cupcakes\nwith chocolate frosting", 38, favsInfo.charAt(4) == '1' ? true : false, 4, R.drawable.big_5, "These cupcakes have a creamy vanilla base and are topped with rich chocolate frosting. They are ideal for lovers of classic taste."));
        list.add(new Item(R.drawable.capcake_2, "Red Velvet Cupcakes", 35, favsInfo.charAt(5) == '1' ? true : false, 5, R.drawable.big_6, "Red velvet cupcakes have a rich red color and a delicate flavor. Their base consists of a mixture of butter, sugar, eggs and flour, as well as a small amount of cocoa powder and food coloring. These cupcakes are decorated with buttercream and sprinkled with chocolate chips."));
        list.add(new Item(R.drawable.capcake_3, "Carrot cupcakes", 34, favsInfo.charAt(6) == '1' ? true : false, 6, R.drawable.big_7, "Carrot cupcakes are a great choice for those who love unusual flavors. They are based on carrots, which gives them a bright orange color and a sweetish taste. These cupcakes are usually decorated with butter or cheese frosting and may be topped with nuts or candied fruits."));
        list.add(new Item(R.drawable.capcake_4, "Lemon cupcakes", 38, favsInfo.charAt(7) == '1' ? true : false, 7, R.drawable.big_8, "Lemon cupcakes have a refreshing citrus flavor. Their base is made from butter, sugar and eggs, and then lemon juice and zest are added. These cupcakes can be decorated with lemon glaze or sprinkled with powdered sugar."));
    }

    private void fillListChessecakes(List<Item> list){
        SharedPreferences shp = getSharedPreferences("FAV", MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        favsInfo = shp.getString("FAV", "0000000000000000");
        list.add(new Item(R.drawable.cheesecake_1, "Classic cheesecake", 33, favsInfo.charAt(12) == '1' ? true : false, 12, R.drawable.big_13, "This is the most popular type of cheesecake, which consists of a shortcrust pastry base and cream cheese filling. The filling has a creamy texture and delicate taste. The cheesecake is baked in the oven until golden brown."));
        list.add(new Item(R.drawable.cheesecake_2, "New York cheesecake", 35, favsInfo.charAt(13) == '1' ? true : false, 13, R.drawable.big_14, "This type of cheesecake differs from the classic one in that it has a denser and richer filling. The filling includes cream cheese, eggs and sugar. Cheesecake is also baked in the oven, but longer than the classic one."));
        list.add(new Item(R.drawable.cheesecake_3, "Chocolate cheesecake", 35, favsInfo.charAt(14) == '1' ? true : false, 14, R.drawable.big_15, "Chocolate is added to this type of cheesecake, which gives it a rich taste and aroma. Chocolate can be added to the shortcrust pastry base or cream cheese filling. The cheesecake can be decorated with chocolate glaze or sprinkled with chocolate chips."));
        list.add(new Item(R.drawable.cheesecake_4, "Caramel cheesecake", 40, favsInfo.charAt(15) == '1' ? true : false, 15, R.drawable.big_16, "Cheesecake with caramel taste and aroma. To prepare it, caramel is used, which is added to the cream cheese filling or as a topping. The cheesecake is very sweet and rich."));
    }

    private void fillListMuffins(List<Item> list){
        SharedPreferences shp = getSharedPreferences("FAV", MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        favsInfo = shp.getString("FAV", "0000000000000000");
        list.add(new Item(R.drawable.muffin_1, "Chocolate muffins with cherries", 60, favsInfo.charAt(8) == '1' ? true : false, 8, R.drawable.big_9, "These muffins have a rich chocolate taste and aroma, as well as a delicate texture. They are perfect for chocolate lovers. Cherries are used as a filling, which gives the muffins a sweet and sour taste."));
        list.add(new Item(R.drawable.muffin_2, "Banana and chocolate muffins", 65, favsInfo.charAt(9) == '1' ? true : false, 9, R.drawable.big_10, "Muffins filled with banana and chocolate are a combination of delicate banana flavor and rich chocolate flavor. The banana adds sweetness and moisture to the muffin, while the chocolate adds aroma and flavor. These muffins are perfect for breakfast or snack."));
        list.add(new Item(R.drawable.muffin_3, "Lemon muffins", 62, favsInfo.charAt(10) == '1' ? true : false, 10, R.drawable.big_11, "Lemon muffins are a classic American dessert. They have a delicate lemon taste and aroma. Lemon zest gives the muffins a pleasant bitterness, and lemon juice adds sourness. These muffins go great with tea or coffee."));
        list.add(new Item(R.drawable.muffin_4, "Vanilla muffins with berries", 63, favsInfo.charAt(11) == '1' ? true : false, 11, R.drawable.big_12, "Vanilla muffins with berries are a tender and airy treat. Berries give the muffins freshness and juiciness, and vanilla gives a pleasant aroma. These muffins are an excellent choice for tea or dessert after lunch."));
    }

    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        hand.removeCallbacks(runnable);
    }

    public static ConstraintLayout getCard() {
        return card;
    }

    public static ImageView getDark(){
        return dark;
    }

    public static ConstraintLayout getMenuDelete(){
        return menuDelete;
    }

    public static TextView getTotal() {
        return total;
    }

    public static TextView getCounterBasket() { return counterBasket; }

    @Override
    public void onBackPressed() {
        if (idCurrentView == 0) {
            destroySlideMenu();
        } else {
            finish();
        }

    }
}