package test;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.*;
import java.util.*;

public class TextToImgDemo
{
	public static void main(String[] args) {
		String[] sttstring = {"이번","  시간"," 에는","  소피아 디자인의","  관련된 좀 더","  자세한 얘기를 해 보도록 하겠습니다","  앞서 사람들이 그랬을 거라고 나는","  이제 탄다","  실행했을 "," 분리할 수 있는","  소프트","  디젠의 현대 원칙으로 분류할 수 있는","  그런","  것에 대해서 공부를","  한대요 그래서 그만큼","  이제 자주 쓰이는 하고","  이제","  디자인패턴 분야에서는","  어 그래 스프레드 더 많이 쓰이는","  그런","  프레스필드에 대해서 우리가","  알아볼 예정인데 그","  중에서도","  솔리드라고 하는 날씨 마티니","  그","  정리한","  그런 프린스베베 서울","  원칙에 대해서","  원칙에 대해서 살펴보도록 하겠습니다","  여러분들","  나른한 교과서에서는","  이제","  디자인패턴","  디자인패턴을 대해서 간략하게 언급을","  하고 있는데","  사실은 인제 우리가","  교과 과정","  3 4학년 2학기 때","  설계패턴이라고","  하는데 과목이 따로 있고","  그 감옥에서","  억양을","  포엘디자인 패턴들","  23일 가지에 대해서 자세"," 하게에 대해"," 프렌즈","  프렌즈 디자"," 인패턴을 이해하"," 기 위해서","  연필에 대해 알아야 되는 그런","  설계 관련된","  기본적인","  원칙이라고도 보시면 되겠습니다","  그래서","  솔리드","  대해서 이제 우리가","  공부 해 보도록 하고","  20 자세한","  내용은 제가 이제","  별첨으로","  드는","  쏠리는 프린스페 관련된","  아티클에 읽어보지는","  솔리드프로 어떤","  내용을 담고 있는지 그리고","  스케줄 어떻게","  되는지","  더 자세히 볼 수가 있겠습니다","  우리 4학년 2학기","  때","  설계패턴이라고","  하는 방법을 배운다 그랬죠","  그래서 패턴에서는","  돼 목포에","  디자인패턴","  23가지의","  디자인패턴을 정리라고 있는데","  그 중에 한 가지는","  열등전략 배터리 되겠습니다 전략패턴 내용은 간단하게","  정리해서 본다면","  2절 나타내는 것은","  알고리즘에 분들","  정해 라고요","  그리고 그러한 알고리즘들을","  위챗 슬레이트 갖고","  각각의 알고리즘들을","  서로"," 변화"," 시킬 수","  있게","  될 수 있게","  만들어","  줍니다","  결국은","  전략패턴 사용하게","  되면","  클라이언트가","  사용하고 있는","  그런","  알고리즘들이","  클라이언트와","  별개로","  독립적으로","  변할 수 있겠지","  알고리즘에","  변경해","  것을 사용하고 있는","  클라이언트 영향을 미치지 않는다는 뜻이겠죠","  만들어집니다","  자 이렇게 이제 우리 어때 목포에","  디자인패턴을 정렬할 수 있겠는데","  사시는 이렇게만 공부를","  한다면 대단히","  부족한","  그리고","  있는","  그런 디자인","  공부입니다","  왜냐면 이거 같은 레벨에서의","  디자인패턴을 치질은 어떤"," 현실에서 일어"," 나는","  그외디자인","  문제에 있어서","  그런 부장님 제가","  최근에 나온","  경우 책에 나온","  그런","  것들과 조금만 틀어지거나","  혹은","  약간의","  확장을","  욕할 때 하는","  거죠 그래서","  오늘은","  좋은 디자인을","  배우는","  순간에는","  디자인패턴 만","  배우면 충분한 것이 아니라","  그것을","  서포트하고 있는 거","  같은데","  만들어야 돼 더","  근본적인","  객체지향성이 원칙을 연달아입니다","  배웠던 글라스락 패턴도","  객체지향 설계 원칙","  알아볼 수가 없고요 아니라는 것을","  서포트하고 있는"};
		ArrayList<String> sttarray = new ArrayList<>(Arrays.asList(sttstring));
		ArrayList<Integer> change = new ArrayList<>();
		change.add(50); // 설계 관련된~
		change.add(70);
		ArrayList<Point> index = new ArrayList<>();
		index.add(new Point(6, 10)); //앞서 사람들이 그랬을 거라고~
		index.add(new Point(30, 36)); //여러분들","  나른한 교과서에서는","  이제","  디자인패턴~
		index.add(new Point(50, 55));
		index.add(new Point(60, 70)); // 드는~
		System.out.println(sttstring[50]);
		
		ArrayList<Note> notes = new ArrayList<>();
		ArrayList<BufferedImage> imgs = new ArrayList<>();
		PDFandIMG pdfandimg = new PDFandIMG();
		
		try {
			imgs = pdfandimg.PDFtoIMG("0.pdf");
			IMG_Resize resize = new IMG_Resize(imgs.get(0).getWidth()/2,imgs.get(0).getHeight()/2);
			int i = 0;
			for(BufferedImage note : imgs) {
				notes.add(new Note(resize.ResizeIMG(note),index.get(i).x,index.get(i).y));
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextToImg textToimg = new TextToImg(sttarray,notes,change, imgs.get(0).getWidth(), imgs.get(0).getHeight());
		//textToImg 실행
	}
 
}


