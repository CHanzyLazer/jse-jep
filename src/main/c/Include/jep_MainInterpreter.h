/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class jep_MainInterpreter */

#ifndef _Included_jep_MainInterpreter
#define _Included_jep_MainInterpreter
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     jep_MainInterpreter
 * Method:    setInitParams
 * Signature: (IIIIIIILjava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jep_MainInterpreter_setInitParams
  (JNIEnv *, jclass, jint, jint, jint, jint, jint, jint, jint, jstring, jstring);

/*
 * Class:     jep_MainInterpreter
 * Method:    initializePython
 * Signature: ([Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jep_MainInterpreter_initializePython
  (JNIEnv *, jclass, jobjectArray);

/*
 * Class:     jep_MainInterpreter
 * Method:    sharedImportInternal
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jep_MainInterpreter_sharedImportInternal
  (JNIEnv *, jclass, jstring);

#ifdef __cplusplus
}
#endif
#endif
