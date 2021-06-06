package java.util;

/*
 *  @项目名：  Componentization
 *  @包名：    java.util
 *  @文件名:   AMSRegister
 *  @创建者:   fanlelong
 *  @创建时间:  2021/6/5 9:25 PM
 *  @描述：    TODO
 */

import android.content.Context;
import android.os.BinderProxy;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManagerNative;

import com.android.internal.os.BinderInternal;
import com.android.server.SystemServer;
import com.android.server.SystemServiceManager;
import com.android.server.am.ActivityManagerService;

class AMSRegister {
            //  SystemServer.java
//   main();
//   new SystemServer().run();
//       1:SystemServiceManager mSystemServiceManager = new SystemServiceManager(mSystemContext);
//       2:LocalServices.addService(SystemServiceManager .class, mSystemServiceManager);
//       3:startBootstrapServices();
//           3.1:mActivityManagerService = mSystemServiceManager.startService(ActivityManagerService.Lifecycle.class).getService();
//               3.1.0:className = ActivityManagerService.Lifecycle
//               3.1.1:final Class<SystemService> serviceClass;
//               3.1.2:serviceClass = (Class<SystemService>)Class.forName(className);
//               3.1.3:startService(serviceClass);
//                   3.1.3.1:Constructor<T> constructor = serviceClass.getConstructor(Context.class);
//                   3.1.3.2:service = constructor.newInstance(mContext);
//                       3.1.3.2.0:ActivityManagerService mService = new ActivityManagerService(context); 执行上面构造方法会先实例出mService
//                   3.1.3.3:startService(service);
//                       3.1.3.3.0: private final ArrayList<SystemService> mServices = new ArrayList<SystemService>();
//                       3.1.3.3.1: mServices.add(service);
//                       3.1.3.3.1: service.onStart();
//                           3.1.3.3.1.0:mService->ActivityManagerService;
//                           3.1.3.3.1.1:mService.start();
//                   3.1.3.4:return service;
//           3.2:mActivityManagerService->创建完成,就是上面的return service;
//           3.3:mActivityManagerService.setSystemProcess();
//               3.3.1:ServiceManager.addService(Context.ACTIVITY_SERVICE, this, true);
//                   3.3.1.1:getIServiceManager().addService(name, service, allowIsolated);
//                       3.3.1.1.1:getIServiceManager();
//                           3.3.1.1.1.0:private static IServiceManager sServiceManager;
//                           3.3.1.1.1.1:sServiceManager = ServiceManagerNative.asInterface(BinderInternal.getContextObject());
//                               3.3.1.1.1.1.1:BinderInternal.getContextObject();
//                                   3.3.1.1.1.1.1.1:public static final native IBinder getContextObject();
//                                       3.3.1.1.1.1.1.1.1:android_util_Binder.cpp里对应的native方法-->{ "getContextObject", "()Landroid/os/IBinder;", (void*)android_os_BinderInternal_getContextObject },
//                                           3.3.1.1.1.1.1.1.1.1:sp<IBinder> b = ProcessState::self()->getContextObject(NULL);
//                                               3.3.1.1.1.1.1.1.1.1.1:ProcessState::self()
//                                                   3.3.1.1.1.1.1.1.1.1.1.1:gProcess = new ProcessState;
//                                                       3.3.1.1.1.1.1.1.1.1.1.1.1:ProcessState构造方法: mDriverFD(open_driver())
//                                                           3.3.1.1.1.1.1.1.1.1.1.1.1.1:ProcessState构造方法: mDriverFD(open_driver())
//                                                           3.3.1.1.1.1.1.1.1.1.1.1.1.1:ProcessState构造方法: mDriverFD(open_driver())
//                                                               3.3.1.1.1.1.1.1.1.1.1.1.1.1:int fd = open("/dev/binder", O_RDWR);
//                                                               3.3.1.1.1.1.1.1.1.1.1.1.1.2:esult = ioctl(fd, BINDER_SET_MAX_THREADS, &maxThreads);
//                                                           3.3.1.1.1.1.1.1.1.1.1.1.1.2: mVMStart = mmap(0, BINDER_VM_SIZE, PROT_READ, MAP_PRIVATE | MAP_NORESERVE, mDriverFD, 0);
//                                               3.3.1.1.1.1.1.1.1.1.2:getContextObject(NULL);
//                                                   3.3.1.1.1.1.1.1.1.1.2.1:ProcessState.cpp-->getStrongProxyForHandle(0);
//                                                       3.3.1.1.1.1.1.1.1.1.2.1.0:ProcessState e = lookupHandleLocked(handle);
//                                                       3.3.1.1.1.1.1.1.1.1.2.1.1:b = new BpBinder(handle);
//                                                       3.3.1.1.1.1.1.1.1.1.2.1.2:e->binder = b;
//                                           3.3.1.1.1.1.1.1.1.2:return javaObjectForIBinder(env, b);  b--->3.3.1.1.1.1.1.1.1.1:sp<IBinder> b  返回的就是BinderProxy
//                                               3.3.1.1.1.1.1.1.1.2.1:jobject object = (jobject)val->findObject(&gBinderProxyOffsets); 查找 object是否有
//                                               3.3.1.1.1.1.1.1.1.2.2:object = env->NewObject(gBinderProxyOffsets.mClass, gBinderProxyOffsets.mConstructor); 没有主创建一个
//                                               3.3.1.1.1.1.1.1.1.2.3:env->SetLongField(object, gBinderProxyOffsets.mObject, (jlong)val.get());  val是return javaObjectForIBinder(env, b)这里传过来的b ,也就是BpBinder
//                                                   3.3.1.1.1.1.1.1.1.2.3.1:上面一行代码的意思就是把BpBinder设置给了gBinderProxyOffsets.mObject, 而gBinderProxyOffsets是一个结构体:binderproxy_offsets_t,他里面的jfieldID mObject设置成了BpBinder
//                                                   3.3.1.1.1.1.1.1.1.2.3.2:而gBinderProxyOffsets对应我们java里面的代码就是BinderProxy, 所以BinderProxy 里的这个变量private long mObject;就变成了BpBinder地址了
//                                               3.3.1.1.1.1.1.1.1.2.4:val->attachObject(&gBinderProxyOffsets, refObject, jnienv_to_javavm(env), proxy_cleanup);
//                                                   3.3.1.1.1.1.1.1.1.2.4.1:因為val是BpBinder所以走的是 BpBinder.attachObject所以會走下面代碼
//                                                   3.3.1.1.1.1.1.1.1.2.4.2:e->mObjects.attach(objectID, object, cleanupCookie, func); mObjects BpBinder::ObjectManager mObjects;
//                                                       3.3.1.1.1.1.1.1.1.2.4.1,1:上面代碼的意思就是將BinderProxy與BpBinder進行綁定,這樣二個互相綁定就完成
//                               3.3.1.1.1.1.2:ServiceManagerNative.asInterface
//                                   3.3.1.1.1.1.2.1:return new ServiceManagerProxy(obj);obj: BinderProxy;
//                       3.3.1.1.2:getIServiceManager().addService(name, service, allowIsolated);    getIServiceManager()返回的是上面return的值 ServiceManagerProxy
//                       3.3.1.1.3:所以ServiceManagerProxy.addService(name, service, allowIsolated);
//                           3.3.1.1.3.0:mRemote 是  3.3.1.1.1.1.2.1:return new ServiceManagerProxy(obj) 里的obj 也就是BinderProxy
//                           3.3.1.1.3.1:Parcel data = Parcel.obtain();
//                           3.3.1.1.3.2:Parcel reply = Parcel.obtain();
//                           3.3.1.1.3.3:data.writeInterfaceToken(IServiceManager.descriptor);
//                           3.3.1.1.3.3:data.writeStrongBinder(service); 这个service就是ActivityManagerService 这行代码的意思是把service打包进data里
//                               3.3.1.1.3.3.1:nativeWriteStrongBinder(mNativePtr, val);
//                                   3.3.1.1.3.3.1.0:private static native void nativeWriteStrongBinder(long nativePtr, IBinder val);
//                                   3.3.1.1.3.3.1.1:{"nativeWriteStrongBinder",   "(JLandroid/os/IBinder;)V", (void*)android_os_Parcel_writeStrongBinder} 进入native层的android_os_Parcel_writeStrongBinder方法
//                                   3.3.1.1.3.3.1.2:android_os_Parcel_writeStrongBinder(JNIEnv* env, jclass clazz, jlong nativePtr, jobject object)
//                                       3.3.1.1.3.3.1.2.1:Parcel* parcel = reinterpret_cast<Parcel*>(nativePtr);将我们传进来的nativePtr 转为c层里的Parcel
//                                       3.3.1.1.3.3.1.2.2:const status_t err = parcel->writeStrongBinder(ibinderForJavaObject(env, object));
//                                       3.3.1.1.3.3.1.2.3:Parcel.cpp里的 status_t Parcel::writeStrongBinder(const sp<IBinder>& val)
//                                           3.3.1.1.3.3.1.2.3.1:return flatten_binder(ProcessState::self(), val, this);
//                                           3.3.1.1.3.3.1.2.3.1.1:finish_flatten_binder
//                                               3.3.1.1.3.3.1.2.3.1.1.1:out->writeObject(flat, false);  也就是将我们传进来的data保存到flat_binder_object flat里
//                           3.3.1.1.3.4:data.writeString(name);
//                           3.3.1.1.3.5:mRemote.transact(GET_SERVICE_TRANSACTION, data, reply, 0);  mRemote: BinderProxy();
//                               3.3.1.1.3.5.0:public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException
//                               3.3.1.1.3.5.1:return transactNative(code, data, reply, flags);
//                               3.3.1.1.3.5.2:public native boolean transactNative(int code, Parcel data, Parcel reply, int flags) throws RemoteException;
//                               3.3.1.1.3.5.3:{"transactNative",      "(ILandroid/os/Parcel;Landroid/os/Parcel;I)Z", (void*)android_os_BinderProxy_transact}
//                               3.3.1.1.3.5.4:static jboolean android_os_BinderProxy_transact(JNIEnv* env, jobject obj, jint code, jobject dataObj, jobject replyObj, jint flags)
//                                   3.3.1.1.3.5.4.1:Parcel* data = parcelForJavaObject(env, dataObj);获取到我们data里的数据并转成自己的Parcel
//                                   3.3.1.1.3.5.4.2:Parcel* reply = parcelForJavaObject(env, replyObj);获取到我们reply里的数据并转成自己的Parcel
//                                   3.3.1.1.3.5.4.3:IBinder* target = (IBinder*)env->GetLongField(obj, gBinderProxyOffsets.mObject);获取gBinderProxyOffsets的mObject地址,也就是BpBinder
//                                   3.3.1.1.3.5.4.4:target->transact(code, *data, reply, flags); 也就是走的是BpBinder.transact
//                                   3.3.1.1.3.5.4.4:BpBinder.cpp里的方法:status_t BpBinder::transact(uint32_t code, const Parcel& data, Parcel* reply, uint32_t flags);
//                                       3.3.1.1.3.5.4.4.1: status_t status = IPCThreadState::self()->transact(andle, code, data, reply, flags); 走的是IPCThreadState的transact方法
//                                       3.3.1.1.3.5.4.4.2: IPCThreadState.cpp里的方法 status_t IPCThreadState::transact(int32_t handle,uint32_t code, const Parcel& data,Parcel* reply, uint32_t flags)
//                                       3.3.1.1.3.5.4.4.3: writeTransactionData(BC_TRANSACTION, flags, handle, code, data, NULL); BC_TRANSACTION是一个命令要记住
//                                           3.3.1.1.3.5.4.4.3.1: mOut.writeInt32(cmd);将cmd->BC_TRANSACTION这个命令写到mOut里
//                                       3.3.1.1.3.5.4.4.4: err = waitForResponse(reply);
//                                           3.3.1.1.3.5.4.4.4.0: waitForResponse(Parcel *reply, status_t *acquireResult)
//                                           3.3.1.1.3.5.4.4.4.1: talkWithDriver()
//                                               3.3.1.1.3.5.4.4.4.0: status_t IPCThreadState::talkWithDriver(bool doReceive)
//                                               3.3.1.1.3.5.4.4.4.1: const bool needRead = mIn.dataPosition() >= mIn.dataSize(); 读取数据:这次为Null
//                                               3.3.1.1.3.5.4.4.4.2: ioctl(mProcess->mDriverFD, BINDER_WRITE_READ, &bwr) >= 0  BINDER_WRITE_READ 命令
//                                               3.3.1.1.3.5.4.4.4.2: binder.c 里的 static long binder_ioctl(struct file *filp, unsigned int cmd, unsigned long arg)
//                                               3.3.1.1.3.5.4.4.4.3: cmd 为 BINDER_WRITE_READ 所以进入 binder_ioctl_write_read(filp, cmd, arg, thread);
//                                               3.3.1.1.3.5.4.4.4.4: cmd 为 BINDER_WRITE_READ 所以进入 binder_ioctl_write_read(filp, cmd, arg, thread);
//                                               3.3.1.1.3.5.4.4.4.5: ret = binder_thread_write(proc, thread,bwr.write_buffer,bwr.write_size,med);
//                                               3.3.1.1.3.5.4.4.4.6: 执行BC_TRANSACTION命令
//                                               3.3.1.1.3.5.4.4.4.7:binder_transaction(proc, thread, &tr, cmd == BC_REPLY, 0);   cmd == BC_REPLY 这个返回的是false 第四个参数
//                                               3.3.1.1.3.5.4.4.4.8:static void binder_transaction(struct binder_proc *proc, struct binder_thread *thread,struct binder_transaction_data *tr, int reply, binder_size_t extra_buffers_size)
//                                                   3.3.1.1.3.5.4.4.4.8.1: binder_node *target_node = context->binder_context_mgr_node; 这个target_node也就是SM
//                                                   3.3.1.1.3.5.4.4.4.8.2: binder_node代表的是Binder对象  binder_ref 代表的是Binder引用
//                                                   3.3.1.1.3.5.4.4.4.8.3: struct binder_proc *target_proc =  target_node->proc;//获取proc对象
//                                                   3.3.1.1.3.5.4.4.4.8.4: struct list_head *target_list = &target_proc->todo;;wait_queue_head_t *target_wait = &target_proc->wait; 获取todo t wait
//                                                   3.3.1.1.3.5.4.4.4.8.5: 初始化t和tcomplete  (struct binder_transaction *t; struct binder_work *tcomplete;) t相对于是可以获取SM里的一些东西
//                                                   3.3.1.1.3.5.4.4.4.8.6: t->buffer = binder_alloc_buf(target_proc, tr->data_size, tr->offsets_size, extra_buffers_size, !reply && (t->flags & TF_ONE_WAY)); 开辟一块共享区间
//                                                   3.3.1.1.3.5.4.4.4.8.7: copy_from_user(t->buffer->data, (const void __user *)(uintptr_t) tr->data.ptr.buffer, tr->data_size) 将数据从用户空间拷贝到内核空间
//                                                   3.3.1.1.3.5.4.4.4.8.8: thread->transaction_stack = t; 将t保存到binder_thread *thread里
//                                                   3.3.1.1.3.5.4.4.4.8.9: t->work.type = BINDER_WORK_TRANSACTION;  t的命令是发送给SM的  让Server做事
//                                                   3.3.1.1.3.5.4.4.4.8.9: tcomplete->type = BINDER_WORK_TRANSACTION_COMPLETE; tcomplete的命令是给Client的 让Client挂机
//                                                   3.3.1.1.3.5.4.4.4.8.a: wake_up_interruptible(target_wait); 唤醒SM
//                                           3.3.1.1.3.5.4.4.4.2: 执行BC_TRANSACTION这个命令



    public static void aa (){
        Class<ActivityManagerService.Lifecycle> lifecycleClass = ActivityManagerService.Lifecycle.class;
        lifecycleClass.getService();

    }

}
