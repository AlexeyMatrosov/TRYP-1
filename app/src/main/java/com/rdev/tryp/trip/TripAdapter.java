package com.rdev.tryp.trip;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rdev.tryp.R;
import com.rdev.tryp.model.Driver;
import com.rdev.tryp.model.DriversItem;
import com.rdev.tryp.tryp_car.TrypCarFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

class TripAdapter extends RecyclerView.Adapter {
    List<?> drivers;
    public static final int TYPE_CAR = 1;
    public static final int TYPE_DRIVER = 2;
    int type;
    private OnItemClickListener listener;
    private Context context;

    TripFragment.OrderInterface orderInterface;


    public interface OnItemClickListener {
        void onItemClick(Object item);
    }

    public TripAdapter(List<?> drivers, int type, TripFragment.OrderInterface orderInterface, OnItemClickListener listener, Context context) {
        this.drivers = drivers;
        this.orderInterface = orderInterface;
        this.type = type;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (type == TYPE_CAR) {
            return new CarHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_car_item, parent, false));
        } else {
            return new DriverHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false));
        }


    }

    class CarHolder extends RecyclerView.ViewHolder {
        ImageView car_iv;
        TextView category;
        TextView car_type;
        TextView num_of_passangers;
        TextView num_of_baggage;
        TextView fare_tv;
        CardView price_card_view;

        public CarHolder(@NonNull View itemView) {
            super(itemView);
            car_iv = itemView.findViewById(R.id.avatar_iv);
            category = itemView.findViewById(R.id.category_tv);
            car_type = itemView.findViewById(R.id.car_type);
            num_of_passangers = itemView.findViewById(R.id.num_of_passangers);
            num_of_baggage = itemView.findViewById(R.id.num_of_baggage);
            fare_tv = itemView.findViewById(R.id.fare);
            price_card_view = itemView.findViewById(R.id.price_card_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(drivers.get(getAdapterPosition()));
                }
            });
        }
    }

    class DriverHolder extends RecyclerView.ViewHolder {
        ImageView avatar_iv;
        TextView driver_tv;
        TextView category_tv;
        TextView fare_tv;
        CardView price_card_view;

        public DriverHolder(@NonNull View itemView) {
            super(itemView);
            avatar_iv = itemView.findViewById(R.id.avatar_iv);
            driver_tv = itemView.findViewById(R.id.driver_name);
            category_tv = itemView.findViewById(R.id.driver_category);
            fare_tv = itemView.findViewById(R.id.fare);
            price_card_view = itemView.findViewById(R.id.price_card_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(drivers.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CarHolder) {
            DriversItem item = (DriversItem) drivers.get(position);
            CarHolder carHolder = ((CarHolder) holder);

            //Glide.with(holder.itemView).load(item.getVehicle().getImage()).into(carHolder.car_iv);
            carHolder.car_iv.setImageDrawable(ContextCompat.getDrawable(context,
                    TrypCarFragment.getImageByType(item.getCategory())));
            //Log.i("adapter", "" + item.getCategory());
            carHolder.category.setText(item.getCategory());
            carHolder.car_type.setText(item.getType());
            carHolder.num_of_passangers.setText(item.getMaxPassenger() + "");
            carHolder.num_of_baggage.setText(item.getMaxLuggage() + "");
            carHolder.fare_tv.setText("$" + item.getFare());
            carHolder.price_card_view.setOnClickListener(v -> {
                if (orderInterface != null)
                    orderInterface.onPriceClick(item);
            });
        } else {
            DriverHolder driverHolder = ((DriverHolder) holder);
            ImageView avatar_iv = driverHolder.avatar_iv;
            TextView driver_tv = driverHolder.driver_tv;
            TextView category_tv = driverHolder.category_tv;
            DriversItem item = (DriversItem) drivers.get(position);
            Glide.with(holder.itemView).load(item.getDriver().getImage()).into(avatar_iv);
            driver_tv.setText(item.getDriver().getFirstName() + " " + item.getDriver().getLastName());
            category_tv.setText(item.getCategory());
            driverHolder.fare_tv.setText( "$" + item.getFare());
            driverHolder.price_card_view.setOnClickListener(v -> {
                if (orderInterface != null)
                    orderInterface.onPriceClick(item);
            });
        }
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }
}
