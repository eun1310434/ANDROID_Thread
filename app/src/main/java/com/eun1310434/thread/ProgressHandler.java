/*=====================================================================
□ INFORMATION
  ○ Data : 23.05.2018
  ○ Mail : eun1310434@naver.com
  ○ Blog : https://blog.naver.com/eun1310434
  ○ Reference : Do it android app Programming

□ STUDY
  ○ Thread
      - 프로세스 내에서 실행되는 시작점과 종료점이 있는 일련의 작업 흐름 단위.

  ○ Multi-Thread
      - 프로그램 환경에 따라 하나의 프로세스에서 둘 이상의 스레드를 동시에 실행하는 방식(CPU가 하나일 때 시분할 개념에 기반하여 동작)

  ○ Thread Process
      - Thread클래스나 Runnable 인터페이스를 상속받거나 구현하는 클래스를 작성
      - run메서드(: Thread의 메인 메소드)를 오버라이딩 → start메서드로 실행
      - Thread에서 run()을 호출하여 사용하면 MainThread에서 순차적으로 실행
      - Thread에서 start()을 호출하여 사용하면 별개의 프로세스에서 실행되므로 MainThread에 영향을 받지 않고 실행
         즉, start()로 실행해야 별도의 프로세스로 실행됨

  ○ Runnable
      - Thread를 extends하여 구현한 클래스는 다른 클래스를 extends하지 못함
      - Runnable interface로 implements하면 Thread가 아닌 다른 클래스를 extends 가능
      - Custormizing Thread : Only extends Thread
      - Thread(Runnable) : Thread function + Other Class can extends

  ○ Thread 우선순위 (setPriority)
      - 1 ~ 10 사이의 수로 표시
      - MIN_PRIORITY(1)
      -  NORM_PRIORITY(5) : 기본적으로 사용됨
      - MAX_PRIORITY(10)

  ○ Thread Synchronized
      - 멀티 스레드로 작동하는 프로그램에서 여러 스레드가 하나의 공유 메모리를 동시에 사용할 때
        하나의 스레드가 사용 중일 때 다른 스레드가 접근하지 못하도록 막는 것.
      - 임계영역(Critical Section) : 동기화 대상이 되는 코드 영역
      - synchronized()

  ○ Thread Group
      - 스레드를 목적성에 따라 그룹화 하여 관리
      - 그룹화가 된 스레드들은 한번에 속성을 지정하는 것이 가능
         : 우선순위 일괄지정, 데몬 스레드 여부 일관 지정 등..

  ○ Thread Pool
      - 스레드의 최초 생성에 필요한 자원의 낭비를 줄이고자 한번 생성한 스레드를 재사용하는것.
      - 프로듀서(Producer) - 컨슈머(Consumer) 패턴 사용
         : Queue라는 Task을 보관하는 영역을 두어 Producer가 요청하는 작업을 차례대로 Queue에 쌓는다.
           쌓인 작업은 정해진 작업 처리 순서(FIFO, LIFO 등)에 따라 Consumer가 하나씩 처리한다.
           이때 Consumer는 처리 순서에 따라 계속해서 작업이 실행이 되어야 하기 때문에
           해당 객체를 매번 생성하고 소멸시키지 않는다(매번 생성하고 소멸시키게 되면 시스템의 부하를 그만큼 높이는 것이 되기 때문)
           따라서 컨슈머는 작업을 풀(Pool)에 보관해 두었다가 프로듀서의 요청이 있을 때마다 즉시 실행 처리를 하게 된다.

  ○ Android - Thread
     - 안드로이드에서 메모리에 Thread를 활용 시 Main Thread와 동시 접근되어 데드락 발생
     - Handler를 통해 사용

  ○ Android - Looper
     - MainThread 에서만 Handler를 통해서 UI접근 가능므로 과다한 작업이 발생되면 성능저하가 생김.
     - Thread를 활용하는 과다한 작업 발생시 해결방법 → 별도의 Thread를 만들어 작업한 뒤 MainThread에 Message로 전달 ( O )
     - 단 UI에 영향을 주려면 MainThread에 선언한 Handler를 통해서만 접근가능
     - 구상
       01) MainThread에서 작업 처리가 아닌 별도의 Thread를 만듬.
           * UI와 관련있는 클래스에서 생성하면 MainThread에서 생성되므로 별도의 클래스에서 생성해야 됨.
           * 생성자에 생성하면 안됨
       02) 별도의 Thread에서 전달한 정보를 받아 처리 할 수 있도록 MainThread에 Handler만 작성
       03) 별도의 Thread에서 MainThread의 Handler에 Message 전송

  ○ 학습체크
      - 스레드와 멀티스레드란?
      - 자바 스레드 작성법은?
      - 스레드의 우선순위란?
      - 스레드의 동기화 방법은?
      - 스레드 그룹의 사용 목적은?
      - 스레드 풀의 사용 목적은?


□ FUNCTION
  ○ 스레드를 활용한 프로그레스바
  ○ 안드로이드에서 메모리에 Thread를 활용 시 Main Thread와 동시 접근되어 데드락 발생
     - Handler를 통해 접근.
=====================================================================*/


package com.eun1310434.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ProgressHandler extends Handler {
    //인터페이스를 활용하여 변경시 연결을 위한 리스너 새로 정의
    //innerClass
    public interface OnProgressListener {
        void onProgressChanged(int incrementValue);
    }
    OnProgressListener listener;

    public void handleMessage(Message msg) {
        //Bundle에 담음 메세지를 갖고옴
        Bundle bundle = msg.getData();
        int incrementValue =bundle.getInt("incrementValue");
        String displayValue =bundle.getString("displayValue");

        listener.onProgressChanged(incrementValue);
    }

    //handleMessage가 처리될 때 UI 객체를 손쉽게 처리하기 위하여 Listener를 활용
    public void setOnProgressListener(OnProgressListener _listener){
        // 해당 클래스를 활용하는 곳에서 setOnProgressListener 선언시
        // OnProgressListener 생성
        this.listener = _listener;
    }
}