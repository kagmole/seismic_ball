package hearc.seismicball.core;

import hearc.seismicball.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class StartMenuActivity extends Activity {

	// get this activity
	public static StartMenuActivity instance = null;

	// Debugging
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Array adapter for the conversation thread
//	private ArrayAdapter<String> mConversationArrayAdapter;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothService mChatService = null;
	
	private Button btnPlay = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_lay);

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		btnPlay = (Button) findViewById(R.id.startLayBtnPlay);
		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendMessage("play");
				startPlay();
			}
		});

		Button btnHighscores = (Button) findViewById(R.id.startLayBtnHighscore);
		btnHighscores.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						HighscoresActivity.class);
				startActivity(intent);
			}
		});

		Button btnOptions = (Button) findViewById(R.id.startLayBtnOption);
		btnOptions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						OptionsActivity.class);
				startActivity(intent);
			}
		});

		Button btnConnect = (Button) findViewById(R.id.btnConnect);
		btnConnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent serverIntent = new Intent(StartMenuActivity.this,
						DeviceListActivity.class);
				startActivityForResult(serverIntent,
						REQUEST_CONNECT_DEVICE_SECURE);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null)
				setupChat();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
	}

	private void setupChat() {
		Log.d(TAG, "setupChat()");

		// Initialize the array adapter for the conversation thread
//		mConversationArrayAdapter = new ArrayAdapter<String>(this,
//				R.layout.message);

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
	protected void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
//			Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show();
			Log.i("TEST","Not connected");
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
			// Toast.makeText(getApplicationContext(),
			// "Un message : " + mOutStringBuffer, Toast.LENGTH_LONG)
			// .show();
			// mOutEditText.setText(mOutStringBuffer);
		}
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					// setStatus(getString(R.string.title_connected_to,
					// mConnectedDeviceName));
//					mConversationArrayAdapter.clear();
					break;
				case BluetoothService.STATE_CONNECTING:
					// setStatus(R.string.title_connecting);
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					// setStatus(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_WRITE:
//				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
//				String writeMessage = new String(writeBuf);
//				mConversationArrayAdapter.add("Me:  " + writeMessage);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				
				if(readMessage.contains("n")){
					Log.i("TEST",readMessage);
				}
				
				if(readMessage.contains("end")&&GameScreen.instance!=null){
					GameScreen.instance.looseGame();
					return;
				}
				
				if(readMessage.contains("next")&&GameScreen.instance!=null){
					GameScreen.instance.goToNextLevel();
					return;
				}
				
				if(readMessage.equals("play")){
					startPlay();
					return;
				}
				
				String value = "";
				int content = 0;
				try {
					value = readMessage.substring(0, 1);
					if(!(value.equals("t")||value.equals("l"))){
						throw new NumberFormatException();
					}
					content = Integer.valueOf(readMessage.substring(1));
				} catch (NumberFormatException e) {
					return;
				}
				
				if (GameScreen.instance != null) {
					if(value.equals("t")){
						GameScreen.instance.ballMultiplayer.setPositionY(content);
					}
					if(value.equals("l")){
						GameScreen.instance.ballMultiplayer.setPositionX(content);
					}
					
				}
				// Toast.makeText(getApplicationContext(), readMessage,
				// Toast.LENGTH_LONG).show();
//				mConversationArrayAdapter.add(mConnectedDeviceName + ":  "
//						+ readMessage);
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};
	
	private void startPlay(){
		instance = StartMenuActivity.this;
		Intent intent = new Intent(getApplicationContext(),
				GameActivity.class);
		startActivity(intent);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, true);
			}
			break;
		case REQUEST_CONNECT_DEVICE_INSECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, false);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occurred
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, "Bluetooth not enabled, leaving..."/*
																		 * R.string
																		 * .
																		 * bt_not_enabled_leaving
																		 */,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void connectDevice(Intent data, boolean secure) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BluetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		mChatService.connect(device, secure);
	}
	
	protected boolean isMultiplayer(){
		if(mChatService.getState()==BluetoothService.STATE_CONNECTED){
			return true;
		}
		return false;
	}
}
