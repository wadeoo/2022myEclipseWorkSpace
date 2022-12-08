package com.sm.lzd.servlet.member;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * �������ͼƬ������֤��
 * */
public class HandleDrawValidateCode extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 120;  //ͼƬ���
	private static final int HEIGHT = 30;  //ͼƬ�߶�
		
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		HttpSession session = request.getSession(true);
		
		//����һ��ͼƬ
		BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		//�õ�ͼƬ
		Graphics graphics = bufferedImage.getGraphics();
		//����ͼƬ����ɫ
		setBackGround(graphics);
		//����ͼƬ�߿�
		setBordor(graphics);
		//��ͼƬ�ϻ������ߣ�����4����ɫ����20������
		drawRandomLine(graphics,Color.GREEN);
		drawRandomLine(graphics,new Color(246,255,145));
		drawRandomLine(graphics,new Color(225,174,252));
		drawRandomLine(graphics,new Color(120,202,254));
		//��ͼƬ��д����ַ�������¼���ɵ�����
		String randomText = drawRandomText((Graphics2D) graphics);
		
		session.setAttribute("checkcode", randomText);
		//������Ӧͷ֪ͨ�������ͼƬ����ʽ��
		response.setContentType("image/jpeg");
		//������Ӧͷ�����������Ҫ����
		response.setDateHeader("expries", -1);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		//��ͼƬд�������
		ImageIO.write(bufferedImage, "jpg", response.getOutputStream());

	}
	
	/**
	 * ����ͼƬ����ɫ
	 * */
	private void setBackGround(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
	}

	/**
	 * ����ͼƬ�߿�
	 * */
	private void setBordor(Graphics graphics) {
		graphics.setColor(Color.BLUE);
		graphics.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);
	}
	
	/**
	 * ��ͼƬ�ϻ�������
	 * */
	private void drawRandomLine(Graphics graphics,Color color) {
		graphics.setColor(color);
		//������������������
		for(int i = 0;i < 5;i++){
			int x1 = new Random().nextInt(WIDTH);
			int x2 = new Random().nextInt(WIDTH);
			int y1 = new Random().nextInt(HEIGHT);
			int y2 = new Random().nextInt(HEIGHT);
			graphics.drawLine(x1, y1, x2, y2);
		}
	}
	
	/**
	 * ��ͼƬ��д����ַ������ֺ���ĸ�����
	 * @param length �ַ����ĳ���
	 * 
	 * @return �������ɵ��ַ�������
	 * */
	private String drawRandomText(Graphics2D graphics) {
		graphics.setColor(Color.RED);
		graphics.setFont(new Font("����", Font.BOLD, 20));
		
		//���ֺ���ĸ�����
		String baseNumLetter = "123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
		StringBuffer sBuffer = new StringBuffer();
		
		int x = 5;  //��תԭ��� x ����
		String ch = "";
		Random random = new Random();
		for(int i = 0;i < 4;i++){
			//����������ת�Ƕ�
			int degree = random.nextInt() % 30;  //�Ƕ�С��30��
			int dot = random.nextInt(baseNumLetter.length());
			ch = baseNumLetter.charAt(dot) + ""; 
			sBuffer.append(ch);
			
			//������ת
			graphics.rotate(degree * Math.PI / 180, x, 20);
			graphics.drawString(ch, x, 20);
			
			//������ת
			graphics.rotate(-degree * Math.PI / 180, x, 20);
			x += 30;
		}
		
		return sBuffer.toString();
	}

	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		doPost(request, response);
	}
}

