package com.shokichi.gamelathinh.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.shokichi.gamelathinh.R;
import com.shokichi.gamelathinh.activities.MainActivity;
import com.shokichi.gamelathinh.models.ReaderDbHelper;
import com.shokichi.gamelathinh.statics.Until;
import com.shokichi.gamelathinh.utils.Card;
import com.shokichi.gamelathinh.utils.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.NOTIFICATION_SERVICE;

public class PraticeGameFragment extends Fragment {
    private static final int COLUMN_PIC = 4;
    private static final int ROW_PIC = 4;

    private List<Bitmap> images = null;
    private List<String> sounds = null;
    private int[][] cards;

    private int level;
    private Drawable hinhUp;
    private Context context;
    private Card card1,card2;
    private int soCapDung = 0;
    OnClickCardButton buttonClick;
    Handler handlerLatHinh,handlerClock;

    private TableLayout tblCards;
    private Button btnExit,btnReset;
    private TextView lblTime,lblStep,lblLevel;

    long timeStart = 0L,timeMini = 0L,timeSwap = 0L,updateTime = 0L;
    int step = 0;

    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeMini = SystemClock.uptimeMillis() - timeStart;
            updateTime = timeSwap + timeMini;
            long seconds = updateTime /1000;
            long minute = seconds/60;
            seconds%=60;
            lblTime.setText(String.format("%02d",minute) + ":"  + String.format("%02d",seconds));
            handlerClock.postDelayed(this,0);
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = ( ViewGroup ) inflater.inflate(R.layout.gameplay,container,false);
        addControls(root);

        // Create and setup the AudioManager to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        newGame();

        addEvents();

        return root;
    }

    public void newGame(){
        initClock();
        step = 0;
        soCapDung = 0;
        handlerClock.postDelayed(runnable, 0);
        tblCards.removeAllViews();
        card1 = null;
        card2 = null;
        lblStep.setText(R.string.step_0);
        for(int i=0;i<ROW_PIC;i++){
            tblCards.addView(addTableRow(i));
        }
        addCards();
    }

    private void addControls(ViewGroup root) {
        hinhUp = getResources().getDrawable(R.drawable.uppicture);
        tblCards = root.findViewById(R.id.tblCards);
        buttonClick = new OnClickCardButton();
        context = tblCards.getContext();
        btnReset = root.findViewById(R.id.btnReset);
        btnExit = root.findViewById(R.id.btnExit);
        lblTime = root.findViewById(R.id.lblTime);
        lblStep = root.findViewById(R.id.lblStep);
        lblLevel = root.findViewById(R.id.lblLevel);
        level = ScreenSlideFragment.level;
        lblLevel.setText("Level: "+level);
        initClock();
        card1 = null;
        card2 = null;


        images = new ArrayList<>();
        sounds = new ArrayList<>();
        importResource();

        handlerLatHinh = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                synchronized (this){
                    xuLyLatCards(msg);
                }
                return false;
            }
        });
    }

    private void initClock() {
        timeMini = 0L ;
        timeStart = 0L ;
        timeSwap = 0L ;
        updateTime = 0L ;
        lblTime.setText("00:00");
        timeStart = SystemClock.uptimeMillis();

        handlerClock = new Handler();
        timeStart = SystemClock.uptimeMillis();
        handlerClock.postDelayed(runnable, 0);
    }

    @Override
    public void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

        }
    }

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };




    private void addEvents() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Thoát");
                builder.setMessage("Bạn có muốn về trang chủ không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Until.removeFragment(getFragmentManager(),PraticeGameFragment.this);
                        Intent intent = new Intent(getContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }

        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có muốn chơi lại không? Quá trình chơi hiện tại sẽ bị mất.");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newGame();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }

    /**
     * import sounds and images in database into list*/
    private void importResource(){
        ReaderDbHelper dbHelper = new ReaderDbHelper(getContext());
        dbHelper.openDatabase();
        SQLiteDatabase database = dbHelper.getMyDatabase();

        Cursor cursor =  database.rawQuery("SELECT * FROM words WHERE level = ?",new String[]{String.valueOf(level)});
        Log.v("Hello",cursor.getColumnIndex("icon")+"");
        while (cursor.moveToNext()){
            byte[] imagesResource = cursor.getBlob(8);
            String audioResourceName = cursor.getString(5);

            Bitmap bitmap = getImageFromByte(imagesResource);
            if(bitmap==null){
                //bitmap = default image
                bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.logo);
            }
            images.add(bitmap);
            sounds.add(audioResourceName);
        }
        cursor.close();
        dbHelper.closeDatabase();


    }

    private void playAudioFromUriResource(String nameAudioResource) {
        releaseMediaPlayer();

        // Request audio focus so in order to play the audio file. The app needs to play a
        // short audio file, so we will request audio focus with a short amount of time
        // with AUDIOFOCUS_GAIN_TRANSIENT.
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // We have audio focus now.

            System.out.println("Name audio resource: " + nameAudioResource);
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + nameAudioResource);
            System.out.println("URI :" + uri.toString());
            mMediaPlayer = MediaPlayer.create(context, uri);
            mMediaPlayer.start();

            // Setup a listener on the media player, so that we can stop and release the
            // media player once the sound has finished playing.
            mMediaPlayer.setOnCompletionListener(mCompletionListener);

            mMediaPlayer.setOnCompletionListener(mCompletionListener);

            // Setup a listener on the media player, so that we can stop and release the
            // media player once the sound has finished playing.
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
        }
    }

    private Bitmap getImageFromByte(byte[] imagesResource){
        if(imagesResource!=null){
            return BitmapFactory.decodeByteArray(imagesResource,0,imagesResource.length);
        }
        return null;
    }

    private void xuLyLatCards(Message msg) {
        if (msg.what == 1) {
            if(cards[card1.getX()][card1.getY()] == cards[card2.getX()][card2.getY()]){
                Log.v("Test game1: ",cards[card1.getX()][card1.getY()]+"");
                Log.v("Test game2: ",cards[card2.getX()][card2.getY()]+"");
                card1.getButton().setVisibility(View.INVISIBLE);
                card2.getButton().setVisibility(View.INVISIBLE);
                soCapDung++;

                if(soCapDung==((COLUMN_PIC*ROW_PIC)/2)){
                    handlerClock.removeCallbacks(runnable);
                    Score score = new Score(lblTime.getText().toString(),level);
                    Until.scoreList.add(score);
                    hienThiThongBao();
                }
            }
            else {
                card1.getButton().setBackground(hinhUp);
                card2.getButton().setBackground(hinhUp);
            }
            card1 = null;
            card2 = null;
        }
    }

    private void hienThiThongBao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("YOU WIN");
        builder.setMessage("Chúc mừng! Bạn đã hoàn thành phần thực hành, bạn có muốn chơi lại không? ");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                newGame();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }




    /**
     * add rows into table*/
    public TableRow addTableRow(int row_index){
        TableRow tableRow = new TableRow(context);
        tableRow.setGravity(Gravity.CENTER);
        for(int i = 0; i< COLUMN_PIC; i++){
            tableRow.addView(createButton(row_index,i));
        }
        return tableRow;
    }

    /**
     * create a button (a card) that is button to process click
     * @param x is button location in row of buttons matrix
     * @param y is button location in column of buttons matrix*/
    public Button createButton(int x,int y){
        Button btnCard = new Button(context);
        btnCard.setBackground(hinhUp);
        btnCard.setId(100*x+y);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int lengthItem = width/5;
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                lengthItem,lengthItem
        );
        params.setMargins(8,8,8,8);

        //Init new Card

        btnCard.setLayoutParams(params);
        btnCard.setOnClickListener(buttonClick);
        return btnCard;
    }




    /**
     * Add a cards array to save image location => To match with images array*/
    public void addCards(){
        cards = new int[ROW_PIC][COLUMN_PIC];
        int size = ROW_PIC * COLUMN_PIC;
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<size;i++){
            list.add(i);
        }
        Random ran = new Random();
        for(int i=size-1;i>=0;i--) {
            int t=0;

            if(i>0){
                t = ran.nextInt(i);
            }

            //Xóa số ở vị trí thứ t trong list đồng thời lấy số đó gán vào mảng cards
            //=> Lần lượt sẽ lấy hết các số trong list ra => Không bị trùng số
            t = list.remove(t);
            //lấy kết quả chia dư cho 2 => t được 1 cặp số
            cards[i/ COLUMN_PIC][i% COLUMN_PIC] = t%(size/2);

        }
    }


    public class OnClickCardButton implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            synchronized (this){
                if(card1!=null && card2!=null){
                    return;
                }
                int id = v.getId();
                int x = id/100;
                int y = id%100;
                System.out.println("x = "+x+"  y = "+ y);
                latHinh((Button) v,x,y);


                //soSanh();
            }

        }

        private void increateStep(){
            ++step;
            lblStep.setText("Step: "+step);
        }

        private void latHinh(Button v,int x,int y) {
            int index = cards[x][y];
            Log.v("index","x= "+x+" - y= "+y + "index="+index);
            //Set case
            int flag = 1;
            for(int i = 0;i <= x ;i++){
                for(int j=0;j < COLUMN_PIC ;j++){
                    if(i==x && j==y){
                        break;
                    }
                    if(index == cards[i][j]){
                        v.setBackgroundResource(R.drawable.speaker);
                        flag = 0;
                        Log.v("Flag",flag+"");
                        //Play âm nhạc và gán sự kiện click
                        playAudioFromUriResource(sounds.get(index));
                        break;
                    }
                }
                if(flag == 0) break;

            }
            if(flag == 1){
                v.setBackground(new BitmapDrawable(getResources(),images.get(index)));
            }

            if(card1==null){
                card1 = new Card(x,y,v);
                increateStep();
            }
            else{
                if(card1.getX() == x && card1.getY() == y){
                    return;
                }

                card2 = new Card(x,y,v);
                increateStep();

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if(handlerLatHinh !=null){
                            Message message = handlerLatHinh.obtainMessage(1);
                            handlerLatHinh.sendMessage(message);
                        }
                    }
                };

                Timer timer = new Timer();
                timer.schedule(timerTask,1000);

            }
        }


    }

}
