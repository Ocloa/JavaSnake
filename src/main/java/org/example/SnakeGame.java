package org.example;

import javax.swing.JPanel;
import javax.swing.Timer;
//импортируем классы для рисования и работы с графикой
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
//импортируем классы для обработки событий клавиатуры и таймера
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
//классы для работы со списками и генератором случайных чисел
import java.util.HashSet;
import java.util.Random;
import java.util.LinkedList;

public class SnakeGame extends JPanel implements ActionListener{
    private final int width; //ширина игрового поля
    private final int height;//высота игрвого поля
    private final int cellSize;//размер клетки игрового поля
    private final int FRAME_RATE = 20; //частота кадров игры в секунду
    private boolean gameStarted = false;//флаг, указывающий началась ли игра
    private boolean gameOver = false;//флаг, указывающий закончилась ли игра
    private int highScore; //рекорд игры
    private GamePoint food; //точка в которой находится еда
    private GamePoint bonus;//точка в которой находится бонус
    private Direction direction = Direction.RIGHT; //текущее направление движения змейки
    private Direction newDirection = Direction.RIGHT;//новое направление движения змейки, заданное пользователем
    private final LinkedList<GamePoint> snake = new LinkedList<>();//список точек из которых состоит змейка
    private final Random random = new Random();

    public SnakeGame(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.cellSize = width / (20 * 2); //вычисляем размер клетки на основе частоты кадров
        setPreferredSize(new Dimension(width, height)); //устанавливаем размер панели
        setBackground(Color.black);//цвет фона
        int currentScore = 0;
    }
    //запуск игры
    public void startGame() {
        resetGameData();//сброс данных игры
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {//прослушка клавиатуры
                handleKeyEvent(e.getKeyCode());//обаботка нажатия клавиш
            }
        });//обработка нажатия клавиши
        new Timer(2000 / FRAME_RATE , this).start();//запуск таймера для обновления игры
    }
    //обработка нажатия клавиш для управления змейкой и перезапуска игры
    private void handleKeyEvent(final int keyCode) {
        if (!gameStarted) {//проверка старта игры
            if (keyCode == KeyEvent.VK_SPACE) {//запуск игры принажатии на пробел
                gameStarted = true;
            }
        } else if (!gameOver) {//проверка конца игры
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    if (direction != Direction.DOWN) {
                        newDirection = Direction.UP;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != Direction.UP) {
                        newDirection = Direction.DOWN;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != Direction.LEFT) {
                        newDirection = Direction.RIGHT;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != Direction.RIGHT) {
                        newDirection = Direction.LEFT;
                    }
                    break;
            }
        } else if (keyCode == KeyEvent.VK_SPACE) { //при перезапуске игры
            gameStarted = false;
            gameOver = false;
            resetGameData();
        }
    }
    //сброс данных игры
    private void resetGameData() {
        snake.clear();
        snake.add(new GamePoint(width / 2, height / 2));
        generateFood();
        generateBonus();
    }
    //генерация еды
    private void generateFood() {
        do {
            food = new GamePoint(random.nextInt(width / cellSize) * cellSize,
                    random.nextInt(height / cellSize) * cellSize);
        } while (snake.contains(food)); //проверка совпадения с какой-либо точкой змейки
    }
    //генерация бонуса
    private void generateBonus() {
        if (random.nextDouble() < 0.5) { // Вероятность генерации бонуса 50%
            do {
                bonus = new GamePoint(random.nextInt(width / cellSize) * cellSize, random.nextInt(height / cellSize) * cellSize);
            } while (snake.contains(bonus) || bonus.equals(food));
        } else {
            bonus = null;
        }
    }
    //отрисовка компонентов игры
    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        //проверка запуска игры
        if (!gameStarted) {
            printMessage(graphics, "Press Space Bar to Begin Game");//вывод сообщения для начала игры
        } else {
            int currentScore = snake.size();
            final String scoreText = "Score: " + currentScore; // текст с текущим счетом
            graphics.setColor(Color.WHITE); // цвет текста
            graphics.setFont(graphics.getFont().deriveFont(20F)); // шрифт
            graphics.drawString(scoreText, 10, 30); // позиция текста на экране

            Color foodColor = Color.green;
            graphics.setColor(foodColor);
            graphics.fillRect(food.x, food.y, cellSize, cellSize);
            if (bonus != null) {
                Color bonusColor = Color.yellow;
                graphics.setColor(bonusColor);
                graphics.fillRect(bonus.x, bonus.y, cellSize, cellSize);
            }
            Color snakeColor = Color.magenta;//цвет змейки
            for (final var point : snake) { //отрисовка змейки
                graphics.setColor(snakeColor);
                graphics.fillRect(point.x, point.y, cellSize, cellSize);
                //градиентная заливка змейки
                final int newRed = (int) Math.round(snakeColor.getRed() * (0.95));
                final int newBlue = (int) Math.round(snakeColor.getBlue() * (0.95));
                snakeColor = new Color(newRed, 0, newBlue);
            }
            if (gameOver) {
                final int finalScore = snake.size();//размер змейки для вывода результата
                if (finalScore > highScore) {//сравнение с рекордом
                    highScore = finalScore;
                }
                printMessage(graphics, "Your Score: " + finalScore //вывод результата
                        + "\nHigh Score: " + highScore //вывод рекорда
                        + "\nPress Space Bar to Reset");//вывод указания для нового старта
            }
        }
    }
    //вывод сообщений
    private void printMessage(final Graphics graphics, final String message) {
        graphics.setColor(Color.WHITE); //цвет текста
        graphics.setFont(graphics.getFont().deriveFont(30F));//шрифт
        int currentHeight = height / 3;
        final var graphics2D = (Graphics2D) graphics;
        final var frc = graphics2D.getFontRenderContext();
        for (final var line : message.split("\n")) { //проход по каждой строке сообщения
            final var layout = new TextLayout(line, graphics.getFont(), frc);
            final var bounds = layout.getBounds();//границы прямоугольника для текста
            final var targetWidth = (float) (width - bounds.getWidth()) / 2; //расчет горизонтальной координаты для центрирования текста по ширине игрового поля
            layout.draw(graphics2D, targetWidth, currentHeight); //отрисовка строки текста
            currentHeight += graphics.getFontMetrics().getHeight(); //добавление высоты для следующей строки
        }
    }
    //перемещение змейки в соответствии с текущим направлением
    private void move() {
        direction = newDirection;//обновление направления змейки

        final GamePoint head = snake.getFirst();//получение текущей позиции головы змейки
        final GamePoint newHead = switch (direction) {//новая голова
            case UP -> new GamePoint(head.x, head.y - cellSize);
            case DOWN -> new GamePoint(head.x, head.y + cellSize);
            case LEFT -> new GamePoint(head.x - cellSize, head.y);
            case RIGHT -> new GamePoint(head.x + cellSize, head.y);
        };
        snake.addFirst(newHead);//добавление новой головы в начало списка
        if (newHead.equals(food)) {//проверка на столкновение с едой
            generateFood();//генерация новой еды
            generateBonus();
        }
        else if (newHead.equals(bonus)) {//проверка на столкновение с бонусом
            int action = random.nextInt(2); //случайное число от 0 до 1 (включительно)
            switch (action) {
                case 0: // размер змейки увеличивается на 3
                    for (int i = 0; i < 3; i++) {
                        direction = newDirection;
                        snake.addLast(switch (direction) {
                            case UP -> new GamePoint(snake.getLast().x, snake.getLast().y + cellSize);
                            case DOWN -> new GamePoint(snake.getLast().x, snake.getLast().y - cellSize);
                            case LEFT -> new GamePoint(snake.getLast().x + cellSize, snake.getLast().y);
                            case RIGHT -> new GamePoint(snake.getLast().x - cellSize, snake.getLast().y);
                        }); // добавляем три новые точки в конец списка змейки
                    }

                    break;
                case 1: // размер змейки уменьшается на 2
                    if (snake.size() > 2) { // проверка условия, что размер змейки больше двух
                        for (int i = 0; i < 2; i++) {
                            snake.removeLast(); // удаляем две последние точки из списка змейки
                        }
                    }
                    break;
            }
            bonus = null; // удаление бонуса
            generateBonus();
            generateFood();
        }
        else if (isCollision()) {//проверка на столкновение с границами или самой собой
            gameOver = true;//смена флага
            snake.removeFirst();//удаление головы
        }
        else {
            snake.removeLast();//удаление хвоста
        }
    }
    //проверка столкновения змейки с границами экрана или самой собой
    private boolean isCollision() {
        final GamePoint head = snake.getFirst();
        final var invalidWidth = (head.x < 0) || (head.x >= width);
        final var invalidHeight = (head.y < 0) || (head.y >= height);
        if (invalidWidth || invalidHeight) {
            return true;
        }
        return snake.size() != new HashSet<>(snake).size();
    }
    //вызов move для перемещения змеки и перерисовка игрового поля
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (gameStarted && !gameOver) {
            move();
        }
        repaint();
    }
    //запись координат точки на игровом поле
    private record GamePoint(int x, int y) {
    }
    //для направлений движения змейки
    private enum Direction {
        UP, DOWN, RIGHT, LEFT
    }
}