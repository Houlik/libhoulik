package com.houlik.libhoulik.android.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * uuid = UUID.randomUUID();随机生成UUID，但是不使用这个随机生成
 *
 * 以下使用步骤:
 * 1. getBluetoothAdapter 获取BluetoothAdapter
 * 2. checkBluetooth 检测是否支持蓝牙 | 蓝牙LE
 * 3. checkBluetoothIsOpen 检测是否已经打开蓝牙
 * 4. requestEnable 如果未打开将进入打开蓝牙对话框
 * 5. requestVisible 是否设置搜索可视化
 * 6. isSearching 设置是否搜索状态 | startSearch 是否开始搜索 | stopSearch 是否停止搜索
 * 7. searchResult2BroadcastReceiver 开始搜索所有被搜索的设备都在广播中获取
 *
 * 在activity中必须先启动服务端, 同时等待客户端点击连接
 * 点击连接的将成为客户端
 * serverConnect 启动服务端
 * acceptSocket 等待客户端请求连接
 * OnAccept 通过接口执行获取到客户端请求连接后的操作
 * isServer = true; 需要为当前使用者判断是否成为服务端
 *
 * 蓝牙工具
 * Created by houlik on 2018/8/9.
 */

public class BluetoothUtils {

    private final String TAG = "BluetoothUtils";

    private static final BluetoothUtils bluetoothUtils = null;
    //连接之间的唯一标识符ID
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Context context;

    private BluetoothUtils(Context context){
        this.context = context;
    }

    public static BluetoothUtils getInstance(Context context){
        if(bluetoothUtils == null){
            return new BluetoothUtils(context);
        }
        return bluetoothUtils;
    }

    //获取当前设备默认的Adapter
    public BluetoothAdapter getBluetoothAdapter(){
        return BluetoothAdapter.getDefaultAdapter();
    }

    // #####  当前蓝牙 #####
    /**
     * 蓝牙适配器BluetoothAdapter
     *
     * BluetoothAdapter的作用其实跟其它的 Manager 差不多，可以把它当作蓝牙管理器。下面是BluetoothAdapter的常用方法说明。
     * getDefaultAdapter：静态方法，获取默认的蓝牙适配器对象；
     * enable：打开蓝牙功能；
     * disable：关闭蓝牙功能；
     * isEnable：判断蓝牙功能是否打开；
     * startDiscovery：开始搜索周围的蓝牙设备；
     * cancelDiscovery：取消搜索操作；
     * isDiscovering：判断当前是否正在搜索设备；
     * getBondedDevices：获取已绑定的设备列表；
     * setName：设置本机的蓝牙名称；
     * getName：获取本机的蓝牙名称；
     * getAddress：获取本机的蓝牙地址；
     * getRemoteDevice：根据蓝牙地址获取远程的蓝牙设备；
     * getState：获取本地蓝牙适配器的状态；
     * listenUsingRfcommWithServiceRecord：根据名称和UUID创建并返回BluetoothServiceSocket；
     * listenUsingRfcommOn：根据渠道编号创建并返回BluetoothServiceSocket。
     */

    /**
     * 检查是否拥有蓝牙
     * @param bluetoothAdapter 蓝牙适配器
     * @return 返回判断
     */
    public boolean checkBluetooth(BluetoothAdapter bluetoothAdapter){
        if(bluetoothAdapter == null){
            return false;
        }
        return true;
    }

    /**
     * 检查是否蓝牙4.0
     * @param context 上下文
     * @return 返回判断
     */
    public boolean checkBluetooth_LE(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * 检查蓝牙是否已经开启
     * @param bluetoothAdapter 蓝牙适配器
     * @return 返回判断
     */
    public boolean checkBluetoothIsOpen(BluetoothAdapter bluetoothAdapter){
        return bluetoothAdapter.isEnabled();
    }

    /**
     * 直接进入请求蓝牙开启对话框
     * @param activity 使用者
     * @param requestCode 请求码
     */
    public void requestEnable(Activity activity, int requestCode){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent,requestCode);
    }

    /**
     * 开启蓝牙后设置在规定时间内为可视状态
     * 120秒内请求可见
     * @param activity 使用者
     * @param requestCode 请求码
     */
    public void requestVisible(Activity activity, int requestCode){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        activity.startActivityForResult(intent,requestCode);
    }

    /**
     * 是否在搜索中
     * @param bluetoothAdapter 蓝牙适配器
     * @return 返回判断
     */
    public boolean isSearching(BluetoothAdapter bluetoothAdapter){
        return bluetoothAdapter.isDiscovering();
    }

    /**
     * 搜索周围蓝牙
     * @param bluetoothAdapter 蓝牙适配器
     */
    public void startSearch(BluetoothAdapter bluetoothAdapter){
        bluetoothAdapter.startDiscovery();
    }

    /**
     * 停止搜索
     * @param bluetoothAdapter 蓝牙适配器
     */
    public void stopSearch(BluetoothAdapter bluetoothAdapter){
        bluetoothAdapter.cancelDiscovery();
    }

    /**
     * 关闭蓝牙
     * @param bluetoothAdapter 蓝牙适配器
     */
    public void requestDisable(BluetoothAdapter bluetoothAdapter){
        bluetoothAdapter.disable();
    }

    /**
     * 搜索蓝牙设备,并且通过回调执行得到蓝牙设备的操作
     * 搜索中如果无设备将得到null值
     *
     * filter.addAction(BluetoothDevice.ACTION_FOUND);  //发现新设备
     * filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);  //绑定状态改变
     * filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);  //开始扫描
     * filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);  //结束扫描
     * filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);  //连接状态改变
     * filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);  //蓝牙开关状态改变
     *
     * 广播获取搜索的周边蓝牙设备
     * if(intent.getAction() == BluetoothDevice.ACTION_FOUND){
     *                                 //得到搜索到的蓝牙设备
     *                                 BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
     *                                 if(device.getName() != null) {
     *                                     if(!listDeviceName.contains(device.getName())) {
     *                                         listDeviceName.add(device.getName());
     *                                         listDeviceAddress.add(device.getAddress());
     *                                         listBluetoothDevice.add(device);
     *                                         processAdapter();
     *                                     }
     *                                 }
     *                             }
     * @param context 上下文
     * @param broadcastReceiver 广播
     * @param callback 回调
     */
    public BluetoothBroadcastReceiver searchResult2BroadcastReceiver(Context context, BluetoothBroadcastReceiver broadcastReceiver, BluetoothBroadcastReceiver.SearchCallBack callback){
        //广播过滤蓝牙搜索到的设备
        //IntentFilter filter = new IntentFilter();
        //filter.addAction(intentAction);
        //判断之前广播是否还存在,是则注销,否则将重叠执行广播任务
        if(broadcastReceiver != null){
            context.unregisterReceiver(broadcastReceiver);
        }
        //通过广播执行得到设备的操作
        broadcastReceiver = new BluetoothBroadcastReceiver(callback);
        context.registerReceiver(broadcastReceiver, callback.addAction2IntentFilter());
        return broadcastReceiver;
    }


    /**
     * 注销蓝牙搜索广播
     * 使用完毕必须在相关activity onDestroy中注销该广播
     * @param broadcastReceiver 广播
     */
    public void unRegisterSearchBR(BroadcastReceiver broadcastReceiver){
        context.unregisterReceiver(broadcastReceiver);
    }

    // #####  配对蓝牙 #####

    /**
     * 蓝牙设备BluetoothDevice
     * BluetoothDevice用于指某个蓝牙设备，通常表示对方设备。BluetoothAdapter管理的是本机蓝牙设备。下面是BluetoothDevice的常用方法说明。
     * getName：获得该设备的名称；
     * getAddress：获得该设备的地址；
     * getBondState：获得该设备的绑定状态；
     * createBond：创建匹配对象；
     * createRfcommSocketToServiceRecord：根据UUID创建并返回一个BluetoothSocket。
     */

    /**
     * 得到已经连接的列表
     * @param bluetoothAdapter 蓝牙适配器
     * return 返回装着设备信息的set集合
     */
    public Set<BluetoothDevice> getListBluetoothDevice(BluetoothAdapter bluetoothAdapter){
        //已经连接的客户端蓝牙设备信息
        return bluetoothAdapter.getBondedDevices();
    }

    /**
     * 开始进行双方之间配对
     * 配对广播 ACTION_BOND_STATE_CHANGED
     * @param bluetoothDevice
     */
    public void pairBluetoothDevice(BluetoothDevice bluetoothDevice){
        bluetoothDevice.createBond();
    }

    // #####  服务端蓝牙 #####

    /**
     * 蓝牙服务器套接字BluetoothServiceSocket
     *
     * BluetoothServiceSocket是服务端的Socket，用来接收客户端的Socket连接请求。
     * 下面是常用的方法说明。
     * accept：监听外部的蓝牙连接请求；
     * close：关闭服务端的蓝牙监听。
     *
     * 作为服务器连接
     * 当您需要连接两台设备时，其中一台设备必须保持开放的 BluetoothServerSocket，从而充当服务器。
     * 服务器套接字的用途是侦听传入的连接请求，并在接受请求后提供已连接的 BluetoothSocket。
     * 从 BluetoothServerSocket 获取 BluetoothSocket 后，
     * 您可以（并且应该）舍弃 BluetoothServerSocket，除非您的设备需要接受更多连接。
     *
     * 如要设置服务器套接字并接受连接，请依次完成以下步骤：
     *
     * 通过调用 listenUsingRfcommWithServiceRecord() 获取 BluetoothServerSocket。
     * 该字符串是服务的可识别名称，系统会自动将其写入到设备上的新服务发现协议 (SDP) 数据库条目。
     * 此名称没有限制，可直接使用您的应用名称。SDP 条目中也包含通用唯一标识符 (UUID)，这也是客户端设备连接协议的基础。
     * 换言之，当客户端尝试连接此设备时，它会携带 UUID，从而对其想要连接的服务进行唯一标识。
     * 为了让服务器接受连接，这些 UUID 必须互相匹配。
     *
     * UUID 是一种标准化的 128 位格式，可供字符串 ID 用来对信息进行唯一标识。
     * UUID 的特点是其足够庞大，因此您可以选择任意随机 ID，而不会与其他任何 ID 发生冲突。
     * 在本例中，其用于对应用的蓝牙服务进行唯一标识。如要获取供应用使用的 UUID，
     * 您可以从网络上的众多随机 UUID 生成器中任选一种，然后使用 fromString(String) 初始化一个 UUID。
     *
     * 通过调用 accept() 开始侦听连接请求。
     * 这是一个阻塞调用。当服务器接受连接或异常发生时，该调用便会返回。
     * 只有当远程设备发送包含 UUID 的连接请求，并且该 UUID 与使用此侦听服务器套接字注册的 UUID 相匹配时，服务器才会接受连接。
     * 连接成功后，accept() 将返回已连接的 BluetoothSocket。
     *
     * 如果您无需接受更多连接，请调用 close()。
     * 此方法调用会释放服务器套接字及其所有资源，但不会关闭 accept() 所返回的已连接的 BluetoothSocket。
     * 与 TCP/IP 不同，RFCOMM 一次只允许每个通道有一个已连接的客户端，因此大多数情况下，在接受已连接的套接字后，
     * 您可以立即在 BluetoothServerSocket 上调用 close()。
     *
     * 由于 accept() 是阻塞调用，因此您不应在主 Activity 界面线程中执行该调用，这样您的应用才仍然可以响应其他用户的交互。
     * 通常，您可以在应用所管理的新线程中完成所有涉及 BluetoothServerSocket 或 BluetoothSocket 的工作。
     * 如要取消 accept() 等被阻塞的调用，请通过另一个线程，在 BluetoothServerSocket 或 BluetoothSocket 上调用 close()。
     * 请注意，BluetoothServerSocket 或 BluetoothSocket 中的所有方法都是线程安全的方法
     *
     * 例子：
     * private class AcceptThread extends Thread {
     *     private final BluetoothServerSocket mmServerSocket;
     *
     *     public AcceptThread() {
     *         // Use a temporary object that is later assigned to mmServerSocket
     *         // because mmServerSocket is final.
     *         BluetoothServerSocket tmp = null;
     *         try {
     *             // MY_UUID is the app's UUID string, also used by the client code.
     *             tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
     *         } catch (IOException e) {
     *             Log.e(TAG, "Socket's listen() method failed", e);
     *         }
     *         mmServerSocket = tmp;
     *     }
     *
     *     public void run() {
     *         BluetoothSocket socket = null;
     *         // Keep listening until exception occurs or a socket is returned.
     *         while (true) {
     *             try {
     *                 socket = mmServerSocket.accept();
     *             } catch (IOException e) {
     *                 Log.e(TAG, "Socket's accept() method failed", e);
     *                 break;
     *             }
     *
     *             if (socket != null) {
     *                 // A connection was accepted. Perform work associated with
     *                 // the connection in a separate thread.
     *                 manageMyConnectedSocket(socket);
     *                 mmServerSocket.close();
     *                 break;
     *             }
     *         }
     *     }
     *
     *     // Closes the connect socket and causes the thread to finish.
     *     public void cancel() {
     *         try {
     *             mmServerSocket.close();
     *         } catch (IOException e) {
     *             Log.e(TAG, "Could not close the connect socket", e);
     *         }
     *     }
     * }
     *
     * 在此示例中，只需要一个传入连接，因此在接受连接并获取 BluetoothSocket 之后，
     * 应用会立即将获取的 BluetoothSocket 传送到单独的线程、关闭 BluetoothServerSocket 并中断循环。
     *
     * 请注意，如果 accept() 返回 BluetoothSocket，则表示已连接套接字。因此，您不应像从客户端那样调用 connect()。
     *
     * 应用特定的 manageMyConnectedSocket() 方法旨在启动用于传输数据的线程（详情请参阅管理连接部分）。
     *
     * 通常，在完成传入连接的侦听后，您应立即关闭您的 BluetoothServerSocket。
     * 在此示例中，获取 BluetoothSocket 后会立即调用 close()。
     * 此外，您可能还希望在线程中提供一个公共方法，
     * 以便在需要停止侦听服务器套接字时关闭私有 BluetoothSocket。
     */

    /**
     * 服务端
     * @param customName 随意名称
     * @param bluetoothAdapter 蓝牙适配器
     * @return 返回服务端监听器
     */
    public BluetoothServerSocket serverConnect(String customName, BluetoothAdapter bluetoothAdapter){
        try {
            if(bluetoothAdapter != null) {
                return bluetoothAdapter.listenUsingRfcommWithServiceRecord(customName, uuid);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 服务端等待连接状态, 一旦有客户端连接进来,监听的服务端将直接关闭, 从而继续执行单一的通信
     * @param bluetoothServerSocket 服务端监听器
     * @param onAccept 执行其他事务处理的接口
     * @return 返回线程
     */
    public Thread acceptSocket(final BluetoothServerSocket bluetoothServerSocket, final OnAccept onAccept){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                if(bluetoothServerSocket != null) {
                    try {
                        Log.i(TAG, "等待客户连接");
                        //连接中的通信socket
                        BluetoothSocket serverSocket = bluetoothServerSocket.accept();
                        //关闭监听,只连接单一设备
                        bluetoothServerSocket.close();
                        if (serverSocket != null) {
                            if (serverSocket.isConnected()) {
                                //Log.i(TAG, "已有客户端连接");

                                onAccept.serverAction(serverSocket);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * bluetoothSocket.getOutputStream()
     * bluetoothSocket.getInputStream()
     * dataInputStream.readInt()
     * dataInputStream.readUTF()
     */
    public interface OnAccept{
        void serverAction(BluetoothSocket serverSocket);
    }

    // #####  客户端蓝牙 #####

    /**
     * 蓝牙客户端套接字BluetoothSocket
     *
     * BluetoothSocket是客户端的Socket，用于与对方设备进行数据通信。下面是常用的方法说明。
     * connect：建立蓝牙的socket连接；
     * close：关闭蓝牙的socket连接；
     * getInputStream：获取socket连接的输入流对象；
     * getOutputStream：获取socket连接的输出流对象；
     * getRemoteDevice：获取远程设备信息。
     *
     * 作为客户端连接
     * 如果远程设备在开放服务器套接字上接受连接，则为了发起与此设备的连接，您必须首先获取表示该远程设备的 BluetoothDevice 对象。
     * 如要了解如何创建 BluetoothDevice，请参阅查找设备。然后，您必须使用 BluetoothDevice 来获取 BluetoothSocket 并发起连接。
     *
     * 基本步骤如下所示：
     *
     * 使用 BluetoothDevice，通过调用 createRfcommSocketToServiceRecord(UUID) 获取 BluetoothSocket。
     * 此方法会初始化 BluetoothSocket 对象，以便客户端连接至 BluetoothDevice。
     * 此处传递的 UUID 必须与服务器设备在调用 listenUsingRfcommWithServiceRecord(String, UUID) 开放其 BluetoothServerSocket 时所用的 UUID 相匹配。
     * 如要使用匹配的 UUID，请通过硬编码方式将 UUID 字符串写入您的应用，然后通过服务器和客户端代码引用该字符串。
     *
     * 通过调用 connect() 发起连接。请注意，此方法为阻塞调用。
     * 当客户端调用此方法后，系统会执行 SDP 查找，以找到带有所匹配 UUID 的远程设备。
     * 如果查找成功并且远程设备接受连接，则其会共享 RFCOMM 通道以便在连接期间使用，
     * 并且 connect() 方法将会返回。如果连接失败，或者 connect() 方法超时（约 12 秒后），
     * 则此方法将引发 IOException。
     *
     * 由于 connect() 是阻塞调用，因此您应始终在主 Activity（界面）线程以外的线程中执行此连接步骤。
     *
     * 注意：您应始终调用 cancelDiscovery()，以确保设备在您调用 connect() 之前不会执行设备发现。
     * 如果正在执行发现操作，则会大幅降低连接尝试的速度，并增加连接失败的可能性。
     *
     * 例子：
     *
     * private class ConnectThread extends Thread {
     *     private final BluetoothSocket mmSocket;
     *     private final BluetoothDevice mmDevice;
     *
     *     public ConnectThread(BluetoothDevice device) {
     *         // Use a temporary object that is later assigned to mmSocket
     *         // because mmSocket is final.
     *         BluetoothSocket tmp = null;
     *         mmDevice = device;
     *
     *         try {
     *             // Get a BluetoothSocket to connect with the given BluetoothDevice.
     *             // MY_UUID is the app's UUID string, also used in the server code.
     *             tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
     *         } catch (IOException e) {
     *             Log.e(TAG, "Socket's create() method failed", e);
     *         }
     *         mmSocket = tmp;
     *     }
     *
     *     public void run() {
     *         // Cancel discovery because it otherwise slows down the connection.
     *         bluetoothAdapter.cancelDiscovery();
     *
     *         try {
     *             // Connect to the remote device through the socket. This call blocks
     *             // until it succeeds or throws an exception.
     *             mmSocket.connect();
     *         } catch (IOException connectException) {
     *             // Unable to connect; close the socket and return.
     *             try {
     *                 mmSocket.close();
     *             } catch (IOException closeException) {
     *                 Log.e(TAG, "Could not close the client socket", closeException);
     *             }
     *             return;
     *         }
     *
     *         // The connection attempt succeeded. Perform work associated with
     *         // the connection in a separate thread.
     *         manageMyConnectedSocket(mmSocket);
     *     }
     *
     *     // Closes the client socket and causes the thread to finish.
     *     public void cancel() {
     *         try {
     *             mmSocket.close();
     *         } catch (IOException e) {
     *             Log.e(TAG, "Could not close the client socket", e);
     *         }
     *     }
     * }
     *
     * 请注意，此段代码在尝试连接之前先调用了 cancelDiscovery()。
     * 您应始终在 connect() 之前调用 cancelDiscovery()，这是因为无论当前是否正在执行设备发现，cancelDiscovery() 都会成功。
     * 但是，如果应用需要确定是否正在执行设备发现，您可以使用 isDiscovering() 进行检测。
     *
     * 应用特定 manageMyConnectedSocket() 方法旨在启动用于传输数据的线程（详情请参阅管理连接部分）。
     *
     * 使用完 BluetoothSocket 后，请务必调用 close()。这样，您便可立即关闭连接的套接字，并释放所有相关的内部资源。
     */

    /**
     * 获取客户端socket
     * @param bluetoothDevice 客户端设备
     * @return 返回socket
     */
    public BluetoothSocket clientConnect(BluetoothDevice bluetoothDevice){
        try {
            if(Build.VERSION.SDK_INT >= 10) {
                return bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            }else{
                return bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 开始连接到服务器, 如果成功连接, 就可以开始通信
     * @param bluetoothAdapter 蓝牙适配器
     * @param clientSocket 客户端socket
     * @param onConnect 执行其他事务处理的接口
     * @return 返回线程
     */
    public Thread connectSocket(BluetoothAdapter bluetoothAdapter, final BluetoothSocket clientSocket, final OnConnect onConnect){
        //先取消正在搜索的行动
        if(bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        return new Thread(new Runnable() {
            @Override
            public void run() {
                if(!clientSocket.isConnected()){
                    try {
                        clientSocket.connect();

                        onConnect.clientAction(clientSocket);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public interface OnConnect{
        void clientAction(BluetoothSocket socket);
    }

    /**
     * 管理连接
     * 成功连接多台设备后，每台设备都会有已连接的 BluetoothSocket。这一点非常有趣，因为这表示您可以在设备之间共享信息。
     * 使用 BluetoothSocket 传输数据的一般过程如下所示：
     *
     * 使用 getInputStream() 和 getOutputStream()，分别获取通过套接字处理数据传输的 InputStream 和 OutputStream。
     * 使用 read(byte[]) 和 write(byte[]) 读取数据以及将其写入数据流。
     * 当然，您还需考虑实现细节。具体来说，您应使用专门的线程从数据流读取数据，以及将数据写入数据流。
     * 这一点非常重要，因为 read(byte[]) 和 write(byte[]) 方法都是阻塞调用。read(byte[]) 方法将会阻塞，直至从数据流中读取数据。
     * write(byte[]) 方法通常不会阻塞，但若远程设备调用 read(byte[]) 方法的速度不够快，进而导致中间缓冲区已满，则该方法可能会保持阻塞状态以实现流量控制。
     * 因此，线程中的主循环应专门用于从 InputStream 中读取数据。您可使用线程中单独的公共方法，发起对 OutputStream 的写入操作。
     *
     *
     * public class MyBluetoothService {
     *     private static final String TAG = "MY_APP_DEBUG_TAG";
     *     private Handler handler; // handler that gets info from Bluetooth service
     *
     *     // Defines several constants used when transmitting messages between the
     *     // service and the UI.
     *     private interface MessageConstants {
     *         public static final int MESSAGE_READ = 0;
     *         public static final int MESSAGE_WRITE = 1;
     *         public static final int MESSAGE_TOAST = 2;
     *
     *         // ... (Add other message types here as needed.)
     *     }
     *
     *     private class ConnectedThread extends Thread {
     *         private final BluetoothSocket mmSocket;
     *         private final InputStream mmInStream;
     *         private final OutputStream mmOutStream;
     *         private byte[] mmBuffer; // mmBuffer store for the stream
     *
     *         public ConnectedThread(BluetoothSocket socket) {
     *             mmSocket = socket;
     *             InputStream tmpIn = null;
     *             OutputStream tmpOut = null;
     *
     *             // Get the input and output streams; using temp objects because
     *             // member streams are final.
     *             try {
     *                 tmpIn = socket.getInputStream();
     *             } catch (IOException e) {
     *                 Log.e(TAG, "Error occurred when creating input stream", e);
     *             }
     *             try {
     *                 tmpOut = socket.getOutputStream();
     *             } catch (IOException e) {
     *                 Log.e(TAG, "Error occurred when creating output stream", e);
     *             }
     *
     *             mmInStream = tmpIn;
     *             mmOutStream = tmpOut;
     *         }
     *
     *         public void run() {
     *             mmBuffer = new byte[1024];
     *             int numBytes; // bytes returned from read()
     *
     *             // Keep listening to the InputStream until an exception occurs.
     *             while (true) {
     *                 try {
     *                     // Read from the InputStream.
     *                     numBytes = mmInStream.read(mmBuffer);
     *                     // Send the obtained bytes to the UI activity.
     *                     Message readMsg = handler.obtainMessage(
     *                             MessageConstants.MESSAGE_READ, numBytes, -1,
     *                             mmBuffer);
     *                     readMsg.sendToTarget();
     *                 } catch (IOException e) {
     *                     Log.d(TAG, "Input stream was disconnected", e);
     *                     break;
     *                 }
     *             }
     *         }
     *
     *         // Call this from the main activity to send data to the remote device.
     *         public void write(byte[] bytes) {
     *             try {
     *                 mmOutStream.write(bytes);
     *
     *                 // Share the sent message with the UI activity.
     *                 Message writtenMsg = handler.obtainMessage(
     *                         MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
     *                 writtenMsg.sendToTarget();
     *             } catch (IOException e) {
     *                 Log.e(TAG, "Error occurred when sending data", e);
     *
     *                 // Send a failure message back to the activity.
     *                 Message writeErrorMsg =
     *                         handler.obtainMessage(MessageConstants.MESSAGE_TOAST);
     *                 Bundle bundle = new Bundle();
     *                 bundle.putString("toast",
     *                         "Couldn't send data to the other device");
     *                 writeErrorMsg.setData(bundle);
     *                 handler.sendMessage(writeErrorMsg);
     *             }
     *         }
     *
     *         // Call this method from the main activity to shut down the connection.
     *         public void cancel() {
     *             try {
     *                 mmSocket.close();
     *             } catch (IOException e) {
     *                 Log.e(TAG, "Could not close the connect socket", e);
     *             }
     *         }
     *     }
     * }
     *
     * 当构造函数获取必要的数据流后，线程会等待通过 InputStream 传入的数据。
     * 当 read(byte[]) 返回数据流中的数据时，将使用来自父类的 Handler 成员将数据发送到主 Activity。
     * 然后，线程会等待从 InputStream 中读取更多字节。
     *
     * 发送传出数据不外乎从主 Activity 调用线程的 write() 方法，
     * 并传入要发送的字节。此方法会调用 write(byte[])，从而将数据发送到远程设备。
     * 如果在调用 write(byte[]) 时引发 IOException，则线程会发送一条 Toast 至主 Activity，
     * 向用户说明设备无法将给定的字节发送到另一台（连接的）设备。
     *
     * 借助线程的 cancel() 方法，您可通过关闭 BluetoothSocket 随时终止连接。
     * 当您结束蓝牙连接的使用时，应始终调用此方法。
     *
     * 关键类和接口
     * android.bluetooth 包中提供所有 Bluetooth API。以下概要列出了创建蓝牙连接所需的类和接口：
     *
     * BluetoothAdapter
     * 表示本地蓝牙适配器（蓝牙无线装置）。BluetoothAdapter 是所有蓝牙交互的入口点。
     * 借助该类，您可以发现其他蓝牙设备、查询已绑定（已配对）设备的列表、使用已知的 MAC 地址实例化 BluetoothDevice，
     * 以及通过创建 BluetoothServerSocket 侦听来自其他设备的通信。
     * BluetoothDevice
     * 表示远程蓝牙设备。借助该类，您可以通过 BluetoothSocket 请求与某个远程设备建立连接，或查询有关该设备的信息，
     * 例如设备的名称、地址、类和绑定状态等。
     * BluetoothSocket
     * 表示蓝牙套接字接口（类似于 TCP Socket）。这是允许应用使用 InputStream 和 OutputStream 与其他蓝牙设备交换数据的连接点。
     * BluetoothServerSocket
     * 表示用于侦听传入请求的开放服务器套接字（类似于 TCP ServerSocket）。
     * 如要连接两台 Android 设备，
     * 其中一台设备必须使用此类开放一个服务器套接字。当远程蓝牙设备向此设备发出连接请求时，该设备接受连接，然后返回已连接的 BluetoothSocket。
     * BluetoothClass
     * 描述蓝牙设备的一般特征和功能。这是一组只读属性，用于定义设备的类和服务。
     * 虽然这些信息会提供关于设备类型的有用提示，但该类的属性未必描述设备支持的所有蓝牙配置文件和服务。
     * BluetoothProfile
     * 表示蓝牙配置文件的接口。蓝牙配置文件是适用于设备间蓝牙通信的无线接口规范。
     * 举个例子：免提配置文件。如需了解有关配置文件的详细讨论，请参阅使用配置文件。
     * BluetoothHeadset
     * 提供蓝牙耳机支持，以便与手机配合使用。这包括蓝牙耳机配置文件和免提 (v1.5) 配置文件。
     * BluetoothA2dp
     * 定义如何使用蓝牙立体声音频传输配置文件 (A2DP)，通过蓝牙连接将高质量音频从一个设备流式传输至另一个设备。
     * BluetoothHealth
     * 表示用于控制蓝牙服务的健康设备配置文件代理。
     * BluetoothHealthCallback
     * 用于实现 BluetoothHealth 回调的抽象类。您必须扩展此类并实现回调方法，以接收关于应用注册状态和蓝牙通道状态变化的更新内容。
     * BluetoothHealthAppConfiguration
     * 表示第三方蓝牙健康应用注册的应用配置，该配置旨在实现与远程蓝牙健康设备的通信。
     * BluetoothProfile.ServiceListener
     * 当 BluetoothProfile 进程间通信 (IPC) 客户端连接到运行特定配置文件的内部服务或断开该服务连接时，向该客户端发送通知的接口。
     */


    /**
     * 服务端通信
     *
     * 一开启就必须启动的服务端, 用于时刻保持能监听到客户端的连接。
     * 必须判断该设备是成为服务端还是客户端
     *
     * 例子:
     *         @Override
     *         public void setDataOutputStream(OutputStream outputStream) {
     *             serverOutputStream = new DataOutputStream(outputStream);
     *         }
     *
     *         @Override
     *         public void setDataInputStream(InputStream inputStream) {
     *             serverInputStream = new DataInputStream(inputStream);
     *         }
     *
     *         @Override
     *         public void action() {
     *             handler.post(new Runnable() {
     *                 @Override
     *                 public void run() {
     *                     result_connect_tv.setText("已有客户端连接");
     *                 }
     *             });
     *         }
     *
     *         @Override
     *         public void sendReceiveDataStream() {
     *             new Thread(new Runnable() {
     *                 @Override
     *                 public void run() {
     *                     try {
     *                         while(isServerRunning) {
     *                             try {
     *                                 if(serverInputStream.readInt() == 0) {
     *                                     String clientName = serverInputStream.readUTF();
     *                                     FileOutputStream fileOutputStream = new FileOutputStream(getContext().getExternalFilesDir(clientName) + "/houlik.db");
     *                                     long fileLen = serverInputStream.readLong();
     *                                     long lenL = 0;
     *                                     byte[] bytes = new byte[1024];
     *                                     int len = 0;
     *                                     while ((len = serverInputStream.read(bytes)) != -1) {
     *                                         fileOutputStream.write(bytes, 0, len);
     *                                         lenL += len;
     *                                         if (lenL >= fileLen) {
     *                                             break;
     *                                         }
     *                                     }
     *                                     handler.post(new Runnable() {
     *                                         @Override
     *                                         public void run() {
     *                                             result_connect_tv.setText("文件接收完毕");
     *                                         }
     *                                     });
     *
     *                                     fileOutputStream.flush();
     *                                     fileOutputStream.close();
     *                                 }
     *                             }catch (Exception e){
     *                                 e.printStackTrace();
     *                                 isServerRunning = false;
     *                                 handler.post(new Runnable() {
     *                                     @Override
     *                                     public void run() {
     *                                         result_connect_tv.setText("客户端已断开连接");
     *                                     }
     *                                 });
     *                             }
     *                         }
     *                     } catch (Exception e) {
     *                         e.printStackTrace();
     *                         isServerRunning = false;
     *                     }
     *                 }
     *             }).start();
     *         }
     *
     * @param bluetoothAdapter 蓝牙适配器
     * @param onBluetoothStreamAction 执行发送接收信息的接口
     */
    public void processServerSocket(BluetoothAdapter bluetoothAdapter, final OnBluetoothStreamAction onBluetoothStreamAction){
        //启动服务端
        BluetoothServerSocket bluetoothServerSocket = serverConnect("server", bluetoothAdapter);
        acceptSocket(bluetoothServerSocket, new OnAccept() {
            @Override
            public void serverAction(final BluetoothSocket serverSocket) {
                //do something
                //isServer = true;
                onBluetoothStreamAction.action();

                try {
                    OutputStream outputStream = serverSocket.getOutputStream();
                    onBluetoothStreamAction.setDataOutputStream(outputStream);

                    InputStream inputStream = serverSocket.getInputStream();
                    onBluetoothStreamAction.setDataInputStream(inputStream);

                    onBluetoothStreamAction.sendReceiveDataStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 客户端通信
     * 例子:
     *         @Override
     *         public void setDataOutputStream(OutputStream outputStream) {
     *             clientOutputStream = new DataOutputStream(outputStream);
     *         }
     *
     *         @Override
     *         public void setDataInputStream(InputStream inputStream) {
     *             clientInputStream = new DataInputStream(inputStream);
     *         }
     *
     *         @Override
     *         public void action() {
     *             handler.post(new Runnable() {
     *                 @Override
     *                 public void run() {
     *                     result_connect_tv.setText("已连接到服务端");
     *                 }
     *             });
     *         }
     *
     *         @Override
     *         public void sendReceiveDataStream() {
     *             new Thread(new Runnable() {
     *                 @Override
     *                 public void run() {
     *                     try {
     *                         while(isClientRunning) {
     *                             try {
     *                                 if(clientInputStream.readInt() == 0) {
     *                                     serverName = clientInputStream.readUTF();
     *                                     FileOutputStream fileOutputStream = new FileOutputStream(getContext().getExternalFilesDir(serverName) + "/database.db");
     *                                     long fileLen = clientInputStream.readLong();
     *                                     long lenL = 0;
     *                                     byte[] bytes = new byte[1024];
     *                                     int len = 0;
     *                                     while ((len = clientInputStream.read(bytes)) != -1) {
     *                                         fileOutputStream.write(bytes, 0, len);
     *                                         lenL += len;
     *                                         if (lenL >= fileLen) {
     *                                             break;
     *                                         }
     *                                     }
     *                                     handler.post(new Runnable() {
     *                                         @Override
     *                                         public void run() {
     *                                             result_connect_tv.setText("文件接收完毕");
     *                                         }
     *                                     });
     *
     *                                     fileOutputStream.flush();
     *                                     fileOutputStream.close();
     *                                 }
     *                             }catch (Exception e){
     *                                 e.printStackTrace();
     *                                 isClientRunning = false;
     *                                 handler.post(new Runnable() {
     *                                     @Override
     *                                     public void run() {
     *                                         result_connect_tv.setText("服务端已断开连接");
     *                                     }
     *                                 });
     *                             }
     *                         }
     *                     } catch (Exception e) {
     *                         e.printStackTrace();
     *                         isClientRunning = false;
     *                     }
     *                 }
     *             }).start();
     *         }
     *
     * @param connectDevice 客户端设备
     * @param bluetoothAdapter 蓝牙适配器
     * @param onBluetoothStreamAction 执行发送接收信息的接口
     */
    public void processClientSocket(BluetoothDevice connectDevice, BluetoothAdapter bluetoothAdapter, final OnBluetoothStreamAction onBluetoothStreamAction){
        pairBluetoothDevice(connectDevice);
        BluetoothSocket bluetoothSocket = clientConnect(connectDevice);
        connectSocket(bluetoothAdapter, bluetoothSocket, new BluetoothUtils.OnConnect() {
            @Override
            public void clientAction(BluetoothSocket socket) {
                onBluetoothStreamAction.action();

                try {
                    OutputStream outputStream = socket.getOutputStream();
                    onBluetoothStreamAction.setDataOutputStream(outputStream);

                    InputStream inputStream = socket.getInputStream();
                    onBluetoothStreamAction.setDataInputStream(inputStream);

                    onBluetoothStreamAction.sendReceiveDataStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface OnBluetoothStreamAction{
        void setDataOutputStream(OutputStream outputStream);
        void setDataInputStream(InputStream inputStream);
        void action();
        void sendReceiveDataStream();
    }

    public void cc(){

    }
}
