//
// Created by 伟宸 on 2017/3/8.
//

#include <jni.h>

#define JNIREG_CLASS "com/example/nns_app_1/MainActivity"

extern "C"
JNIEXPORT void JNICALL
Anonymous(JNIEnv *env, jobject instance) {
    //        mailSenderInfo.setMailServerHost("smtp.qq.com");
//        mailSenderInfo.setMailServerPort("465");
//        mailSenderInfo.setValidate(true);
//        mailSenderInfo.setUserName("chenweichen1117@qq.com");
//        mailSenderInfo.setPassword("puhmebaysdombhch");
//        mailSenderInfo.setFromAddress("chenweichen1117@qq.com");
//        mailSenderInfo.setToAddress("chenweichen1117@qq.com");
//        mailSenderInfo.setSubject(mail_subject);
//        mailSenderInfo.setContent(mail_content);

    jclass mainActivity_clazz = env->FindClass("com/example/nns_app_1/MainActivity");
    jclass mailSenderInfo_clazz = env->FindClass("com/example/nns_app_1/MailSenderInfo");
    jfieldID mailSenderInfoID = env->GetStaticFieldID(mainActivity_clazz,
                                                      "mailSenderInfo",
                                                      "Lcom/example/nns_app_1/MailSenderInfo;");
    jobject mailSenderInfo = env->GetStaticObjectField(mainActivity_clazz, mailSenderInfoID);

    jmethodID mSetMailServerHostID = env->GetMethodID(mailSenderInfo_clazz,
                                                      "setMailServerHost", "(Ljava/lang/String;)V");
    jmethodID mSetMailServerPortID = env->GetMethodID(mailSenderInfo_clazz,
                                                      "setMailServerPort", "(Ljava/lang/String;)V");
    jmethodID mSetValidateID = env->GetMethodID(mailSenderInfo_clazz, "setValidate", "(Z)V");
    jmethodID mSetUserNameID = env->GetMethodID(mailSenderInfo_clazz,
                                                "setUserName", "(Ljava/lang/String;)V");
    jmethodID mSetPasswordID = env->GetMethodID(mailSenderInfo_clazz,
                                                "setPassword", "(Ljava/lang/String;)V");
    jmethodID mSetFromAddressID = env->GetMethodID(mailSenderInfo_clazz,
                                                   "setFromAddress", "(Ljava/lang/String;)V");
    jmethodID mSetToAddressID = env->GetMethodID(mailSenderInfo_clazz,
                                                 "setToAddress", "(Ljava/lang/String;)V");

    jstring mailServerHost, mailServerPort, userName, password, fromAddress, toAddress;
    if (!(mailServerHost = env->NewStringUTF("smtp.qq.com"))) return;
    if (!(mailServerPort = env->NewStringUTF("465"))) return;
    if (!(userName = env->NewStringUTF("chenweichen1117@qq.com"))) return;
    if (!(password = env->NewStringUTF("puhmebaysdombhch"))) return;
    if (!(fromAddress = env->NewStringUTF("chenweichen1117@qq.com"))) return;
    if (!(toAddress = env->NewStringUTF("chenweichen1117@qq.com"))) return;


    env->CallVoidMethod(mailSenderInfo, mSetMailServerHostID, mailServerHost);
    env->CallVoidMethod(mailSenderInfo, mSetMailServerPortID, mailServerPort);
    env->CallVoidMethod(mailSenderInfo, mSetValidateID, JNI_TRUE);
    env->CallVoidMethod(mailSenderInfo, mSetUserNameID, userName);
    env->CallVoidMethod(mailSenderInfo, mSetPasswordID, password);
    env->CallVoidMethod(mailSenderInfo, mSetFromAddressID, fromAddress);
    env->CallVoidMethod(mailSenderInfo, mSetToAddressID, toAddress);

}

static JNINativeMethod gMethods[] = {
        {"initSendMailSenderInfo", "()V", (void *)Anonymous},
};

static int registerNativeMethods(JNIEnv *env, const char* className,
                                 JNINativeMethod *gMethods, int numMethods) {
    jclass clazz;
    clazz = env->FindClass(className);
    if (!clazz) return JNI_FALSE;
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) return JNI_FALSE;
    return JNI_TRUE;
}

static int registerNatives(JNIEnv *env) {
    if (!registerNativeMethods(env, JNIREG_CLASS,
                               gMethods, sizeof(gMethods) / sizeof(gMethods[0])))
        return JNI_FALSE;
    return JNI_TRUE;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv* env = 0;
    jint result = -1;

    if((vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK)) {
        return -1;
    }

    if (!registerNatives(env)) return -1;

    result = JNI_VERSION_1_4;

    return result;


}