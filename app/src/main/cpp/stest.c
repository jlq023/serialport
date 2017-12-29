#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <string.h>
  //方法名必须为本地方法的全类名点改为下划线，穿入的两个参数必须这样写，
  //第一个参数为Java虚拟机的内存地址的二级指针，用于本地方法与java虚拟机在内存中交互
  //第二个参数为一个java对象，即是哪个对象调用了这个 c方法
JNIEXPORT jstring JNICALL Java_com_serialportdemo_MainActivity_getStringFromC(JNIEnv* env,
                                                                 jobject obj,jstring msg){
      //定义一个C语言字符串
     char* cstr = "hello form 222c";
     //strcpy (cstr,msg);
     //返回值是java字符串，所以要将C语言的字符串转换成java的字符串
     //在jni.h 中定义了字符串转换函数的函数指针
     //jstring   (*NewStringUTF)(JNIEnv*, const char*);
     //第一种方法：很少用
     jstring jstr1 = (*(*env)).NewStringUTF(env, cstr);
     //第二种方法，推荐
     jstring jstr2 = (*env) -> NewStringUTF(env, cstr);
     //LOGE("JNI====Java_com_serialportdemo_MainActivity_getStringFromC baudrate");
     return jstr2;
 }

char*   Jstring2CStr(JNIEnv*   env,   jstring   jstr)
{
     char*   rtn   =   NULL;
     jclass   clsstring   =   (*env)->FindClass(env,"java/lang/String");
     jstring   strencode   =   (*env)->NewStringUTF(env,"GB2312");
     jmethodID   mid   =   (*env)->GetMethodID(env,clsstring,   "getBytes",   "(Ljava/lang/String;)[B");
     jbyteArray   barr=   (jbyteArray)(*env)->CallObjectMethod(env,jstr,mid,strencode); // String .getByte("GB2312");
     jsize   alen   =   (*env)->GetArrayLength(env,barr);
     jbyte*   ba   =   (*env)->GetByteArrayElements(env,barr,JNI_FALSE);
     if(alen   >   0)
     {
      rtn   =   (char*)malloc(alen+1);         //"\0"
      memcpy(rtn,ba,alen);
      rtn[alen]=0;
     }
     (*env)->ReleaseByteArrayElements(env,barr,ba,0);  //
     return rtn;
}

JNIEXPORT jstring JNICALL Java_com_serialportdemo_MainActivity_encode
  (JNIEnv * env, jobject obj, jstring text, jint length){
      char* cstr = Jstring2CStr(env, text);
      int i;
      for(i = 0;i<length;i++){
     //     *(cstr+i) += 1; //加密算法，将字符串每个字符加1 暂时不需要加
      }
      return (*env)->NewStringUTF(env,cstr);
}

JNIEXPORT jstring JNICALL Java_com_serialportdemo_MainActivity_decode
(JNIEnv * env, jobject obj, jstring text, jint length){
     char* cstr = Jstring2CStr(env, text);
     int i;
     for(i = 0;i<length;i++){
         *(cstr+i) -= 1;
     }
     return (*env)->NewStringUTF(env, cstr);
}

