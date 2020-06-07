﻿# Sticky-Calendar
Sticky Notes + Calender

java의 swing을 활용해 GUI환경의 캘린더를 제작한 프로젝트

기능
- 캘린더 기능
- 일정 추가 / 수정 / 삭제
- 일정에는 날짜, 시간, 설명, 장소, 반복, 카테고리를 설정할 수 있다.
- 캘린더에는 각 일정들이 설정된 색에 따라 라벨 형태로 표시된다.
- 사용자는 캘린더를 화면 가장자리에 띄울 수 있고, 상단 바를 드래그하여 창의 위치를 바꿀 수 있다.

작동 화면
===================
![main](./sample_images/image2.png)

캘린더 화면. 
- 공휴일과 일정 등이 라벨의 형태로 표시된다.
- 화면 상단의 좌우 화살표를 이용해 다음달, 이전달로 이동한다.
- sticky notes 위의 주황색 바를 드래그하여 윈도우 상에서 표시될 위치를 옮길 수 있다.
- 캘린더에서 날짜가 표시된 부분을 클릭하여 일정 리스트 화면으로 이동할 수 있다.

![day](./sample_images/image3.png)

일정 리스트화면.
- 선택된 날짜의 일정들이 리스트로 자세하게 표시된다.
- 상단의 쓰레기통 버튼으로 일정을 삭제하거나, + 버튼으로 새로운 일정을 추가할 수 있다.
- 리스트의 일정을 클릭하면 해당 일정을 수정할 수 있다.

![create](./sample_images/image5.png)

일정 추가/수정화면.
- 일정에 대한 설명, 장소, 시간, 반복 설정을 할 수 있다.
- 음력 설정이나 색상을 통해 카테고리를 설정할 수 있다.
- 선택된 색상은 테두리에 표시되며 캘린더화면에서 선택된 색상으로 라벨을 표시한다.
- 상단의 X 버튼으로 취소하거나 체크버튼으로 저장한다.

![modify](./sample_images/image6.png)

일정 확인화면
- 일정을 저장하거나 일정 리스트 화면에서 일정을 선택하면 해당 일정에 대한 정보들이 자세하게 표시된다.
- 우측 상단의 수정 버튼을 통해 일정 정보를 수정할 수 있다. 일정 수정은 일정 추가와 같은 방식이다.


개발동기 등
=================
java를 배우고 이를 활용한 GUI 환경 프로젝트로 아이디어를 구상하던 중, 윈도우에서 유용하게 사용하고 있던 Sticky Notes와 캘린더를 결합하는 것을 목표로 프로젝트를 진행하기로 하였다.
따라서 Sticky Notes의 UI, 디자인, 기능에 스마트폰의 기본 캘린더 앱을 결합하는 것을 목표로 개발을 진행하였다.

- 개발 기간은 약 2주
- 데이터베이스를 몰라 직접 구현하여 사용했다..
- java swing과 Window Builder 활용
- UI 디자인에 많이 집중한 프로젝트
- Sticky notes 에서 프로젝트 이름을 따와 Sticky Calendar 로 이름지었다.

만들고 싶은 기능들을 대부분 구현하여 만족했지만, 나중에 코드를 다시 살펴보니 가독성이 좋지 않아 수정하기는 어려웠다.
그래서 이후 프로젝트부터는 가독성을 올리고 깔끔하게 구현하는 것을 목표로 개발을 진행하게 되었다.


문제 발생과 해결과정, 구현 방법
=====================

레이아웃 관련
----------------------

swing을 활용해 GUI 환경을 구성했다. 
대부분의 구성요소들은 JPanel 클래스를 상속받아 기능과 속성을 재정의하여 사용하였다.

UI에 신경을 많이 썼는데, 구상하던 레이아웃을 그대로 구현하기 위해 노트에 와이어프레임을 그려가며 진행하였다.

![layout](./sample_images/layout1.JPG)

이런 식으로 모든 레이아웃을 설계하고, 고쳐가며 화면을 구성했다.

![layout2](./sample_images/layout2.JPG)

캘린더에서 일정을 라벨 형식으로 나타내는데, 이 라벨의 순서를 결정하는 것에서 많은 고민을 했다. 
- 위의 레이아웃 그림에서 날짜는 PlDate, 일정은 PlSchedule 객체를 이용해 표현하는데, 2일 이상의 긴 일정을 라벨 형식으로 표현하기 위해서는 이 일정이 있는 모든 날짜 객체가 같은 순서로 라벨을 배치해야 하나의 일정으로 표시된다.
- 때문에 긴 일정과 짧은 일정이 여럿 겹쳐있으면 순서가 꼬여 하나의 일정이 여러줄에 배치되거나, 일정을 표현할 공간이 남아있음에도 표현이 안되는 경우가 발생했다. 
- 대부분의 일정이 위쪽으로 배치되는데 긴 일정 하나가 짧은 일정에 밀려 아래쪽에 배치되어 일관성을 해치는 경우도 발생했다.

이 문제는 우선순위 큐를 활용하여 보완했다. 
- 먼저 일정 객체가 화면에 배치되는 순서를 결정하는 배치 순서 값을 가지도록 한다. 이는 생성시에 -1로 초기화된다.
- 날짜를 앞에서부터 순서대로 로딩하여 해당 날짜에서 배치해야 하는 일정 객체의 배치 순서값이 -1이면 해당 일정의 배치 순서값을 결정한다.
- 배치 순서값은 해당 일정이 지속되는 날짜가 길수록 우선순위가 높도록 구현한 우선순위 큐를 활용해 값을 결정한다.
- 배치 순서값은 해당 일정이 포함된 날짜의 다른 일정이 가질 수 없다.
- 이를 통해 긴 일정이 앞 순서에 배치되도록 유도하여 두가지 문제를 모두 해결하였다.


데이터 관련
--------------------
데이터베이스를 몰라 데이터를 로딩하여 각 날짜 객체가 자신에게 표시되어야 할 정보를 불러오는 과정을 만드는 부분이 힘들었다.
- 이를 구현하기 위해 날짜에 대한 정보들은 날짜 정보 객체와 날짜 표현 객체로 분리하고 로딩 과정을 설계했다. 
- 일정 정보를 로딩할 때에 이 일정이 들어가야할 날짜 정보 객체를 선택하여 일정 정보를 미리 저장해놓는다.
- 날짜 표현 객체가 화면에 표시되어야할 때에 자신과 맞는 정보 객체를 불러와 일정을 적용하는 방식으로 구현했다.


속도 관련
-------------------
레이아웃 매니저를 잘 활용하지 못해 화면을 배치하기 위해 너무 많은 객체를 생성하는 문제가 있다.
- 이로 인해 프로그램 실행시 로딩 속도가 느리다.
- 속도가 느리다는 점이 실행중에는 드러나지 않게하기 위해 현재 표시되는 달력의 이전/다음달의 달력을 미리 생성해 놓는다.
- 사용자가 이전/다음달로 이동하면 해당 달력으로 교체하고, 이후 객체를 재활용하여 데이터만 바꾼다.
- 따라서 달력 객체는 3개만 생성하는 방식으로 구현하여 문제점을 보완하였다.

