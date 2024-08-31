package com.company.transmitter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapterBasket extends RecyclerView.Adapter<ItemAdapterBasket.ViewHolder> {
    private List<ItemBasket> items;
    private Context context;
    int total = 0;

    public ItemAdapterBasket(List<ItemBasket> items, Context context) {
        this.items = items;
        this.context = context;
        for (ItemBasket item : items) {
            total += item.getPrice() * item.getCount();
            TextView ttl = MainActivity.total;

            ttl.setText("Total: " + total);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_basket, parent, false); // todo: itemBasket.xml
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemBasket item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.image.setBackgroundResource(item.getImage());
        holder.price.setText(item.getPrice()+"");
        holder.count.setText(item.getCount()+"");


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isBack = false;
                holder.menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        return;
                    }
                });

                holder.menu.setAlpha(1);
                holder.menu.setZ(2000);
                holder.dark.setAlpha(0.7f);
                holder.dark.setZ(999);

                holder.dark.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        MainActivity.isBack = true;
                        holder.menu.setAlpha(0.0f);
                        holder.menu.setZ(-1);
                        holder.dark.setAlpha(0.0f);
                        holder.dark.setZ(-1);
                        return false;
                    }
                });




                holder.menu.findViewById(R.id.menuDeleteButtonYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.dark.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                        holder.menu.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                return true;
                            }
                        });
                        total -= item.getPrice() * item.getCount();
                        item.setCount(0);
                        holder.total.setText("Total: " + total);
                        int position = holder.getAdapterPosition();
                        SharedPreferences shp = context.getSharedPreferences("basket", MODE_PRIVATE);
                        String backetValue = shp.getString("basket", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
                        String[] stringArray = backetValue.split(",");
                        int[] basketValueInt = new int[stringArray.length];
                        for (int i = 0; i < stringArray.length; i++) {
                            basketValueInt[i] = Integer.parseInt(stringArray[i]);
                        }
                        holder.counter.setText((Integer.parseInt(holder.counter.getText().toString()) - basketValueInt[item.getId()]) + "");
                        basketValueInt[item.getId()] = 0;

                        SharedPreferences.Editor editor = shp.edit();
                        StringBuilder sb = new StringBuilder();
                        for (int i : basketValueInt) {
                            sb.append(i).append(",");
                        }
                        editor.putString("basket", sb.toString());
                        editor.apply();

                        if (position != RecyclerView.NO_POSITION) {
                            // Use Handler to delay the removal
                            new Handler(Looper.getMainLooper()).post(() -> {
                                items.remove(position);
                                notifyItemRemoved(position);
                            });
                        }
                        MainActivity.isBack = true;
                        holder.menu.setAlpha(0.0f);
                        holder.menu.setZ(-1);
                        holder.dark.setAlpha(0.0f);
                        holder.dark.setZ(-1);



                    }
                });

                holder.menu.findViewById(R.id.menuDeleteButtonNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.isBack = true;
                        holder.menu.setAlpha(0.0f);
                        holder.menu.setZ(-1);
                        holder.dark.setAlpha(0.0f);
                        holder.dark.setZ(-1);
                        holder.dark.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                        holder.menu.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                return true;
                            }
                        });

                    }
                });

            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences shp = context.getSharedPreferences("basket", MODE_PRIVATE);
                String backetValue = shp.getString("basket", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
                String[] stringArray = backetValue.split(",");
                int[] basketValueInt = new int[stringArray.length];
                for (int i = 0; i < stringArray.length; i++) {
                    basketValueInt[i] = Integer.parseInt(stringArray[i]);
                }
                if (basketValueInt[item.getId()] < 1000)  {
                    basketValueInt[item.getId()]++;
                    total += item.getPrice();
                    item.setCount(item.getCount()+1);
                    holder.total.setText("Total: " + total);
                }
                MainActivity.counterBasket.setText((Integer.parseInt( MainActivity.counterBasket.getText().toString()) + 1) + "");
                holder.count.setText(basketValueInt[item.getId()]+"");
                SharedPreferences.Editor editor = shp.edit();
                StringBuilder sb = new StringBuilder();
                for (int i : basketValueInt) {
                    sb.append(i).append(",");
                }
                editor.putString("basket", sb.toString());
                editor.apply();
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences shp = context.getSharedPreferences("basket", MODE_PRIVATE);
                String backetValue = shp.getString("basket", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
                String[] stringArray = backetValue.split(",");
                int[] basketValueInt = new int[stringArray.length];
                for (int i = 0; i < stringArray.length; i++) {
                    basketValueInt[i] = Integer.parseInt(stringArray[i]);
                }
                if (basketValueInt[item.getId()] > 1) {
                    basketValueInt[item.getId()]--;
                    total -= item.getPrice();
                    item.setCount(item.getCount()-1);
                    MainActivity.counterBasket.setText((Integer.parseInt( MainActivity.counterBasket.getText().toString()) - 1) + "");
                    holder.total.setText("Total: " + total);
                }


                holder.count.setText(basketValueInt[item.getId()]+"");
                SharedPreferences.Editor editor = shp.edit();
                StringBuilder sb = new StringBuilder();
                for (int i : basketValueInt) {
                    sb.append(i).append(",");
                }
                editor.putString("basket", sb.toString());
                editor.apply();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        ImageView image;
        ImageButton plus;
        ImageButton minus;
        TextView count;
        ImageButton delete;
        ConstraintLayout menu;
        ImageView dark;
        TextView total;
        TextView counter;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            price = itemView.findViewById(R.id.itemPrice);
            image = itemView.findViewById(R.id.itemImage);
            plus = itemView.findViewById(R.id.cardButtonPlus);
            minus = itemView.findViewById(R.id.cardButtonMinus);
            count = itemView.findViewById(R.id.cardCount);
            delete = itemView.findViewById(R.id.itemButtonDelete);
            menu = MainActivity.menuDelete;
            dark = MainActivity.dark;
            total = MainActivity.total;
            counter = MainActivity.counterBasket;
        }
    }
}
