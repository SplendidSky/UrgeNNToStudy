//
// Created by 伟宸 on 2017/3/8.
//

#include <jni.h>

extern "C"
JNIEXPORT void JNICALL
Java_com_example_nns_1app_11_MainActivity_initSendMailSenderInfo(JNIEnv *env, jobject instance) {
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
    jfieldID mailSenderInfoID = env->GetStaticFieldID(mainActivity_clazz, "mailSenderInfo", "Lcom/example/nns_app_1/MailSenderInfo;");
    jobject mailSenderInfo = env->GetStaticObjectField(mainActivity_clazz, mailSenderInfoID);

    jmethodID mSetMailServerHostID = env->GetMethodID(mailSenderInfo_clazz, "setMailServerHost", "(Ljava/lang/String;)V");
    jmethodID mSetMailServerPortID = env->GetMethodID(mailSenderInfo_clazz, "setMailServerPort", "(Ljava/lang/String;)V");
    jmethodID mSetValidateID = env->GetMethodID(mailSenderInfo_clazz, "setValidate", "(Z)V");
    jmethodID mSetUserNameID = env->GetMethodID(mailSenderInfo_clazz, "setUserName", "(Ljava/lang/String;)V");
    jmethodID mSetPasswordID = env->GetMethodID(mailSenderInfo_clazz, "setPassword", "(Ljava/lang/String;)V");
    jmethodID mSetFromAddressID = env->GetMethodID(mailSenderInfo_clazz, "setFromAddress", "(Ljava/lang/String;)V");
    jmethodID mSetToAddressID = env->GetMethodID(mailSenderInfo_clazz, "setToAddress", "(Ljava/lang/String;)V");

    jstring mailServerHost, mailServerPort, userName, password, fromAddress, toAddress;
    if (!(mailServerHost = env->NewStringUTF("smtp.qq.com"))) return;
    if (!(mailServerPort = env->NewStringUTF("465"))) return;
    if (!(userName = env->NewStringUTF("chenweichen1117@qq.com"))) return;
    if (!(password = env->NewStringUTF("puhmebaysdombhch"))) return;
    if (!(fromAddress = env->NewStringUTF("chenweichen1117@qq.com"))) return;
    if (!(toAddress = env->NewStringUTF("chenweichen1117@qq.com"))) return;


    jboolean validate = true;

    env->CallVoidMethod(mailSenderInfo, mSetMailServerHostID, mailServerHost);
    env->CallVoidMethod(mailSenderInfo, mSetMailServerPortID, mailServerPort);
    env->CallVoidMethod(mailSenderInfo, mSetValidateID, validate);
    env->CallVoidMethod(mailSenderInfo, mSetUserNameID, userName);
    env->CallVoidMethod(mailSenderInfo, mSetPasswordID, password);
    env->CallVoidMethod(mailSenderInfo, mSetFromAddressID, fromAddress);
    env->CallVoidMethod(mailSenderInfo, mSetToAddressID, toAddress);


}