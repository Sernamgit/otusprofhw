package ru.otus.prof.streamapi;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        //генерация tasks
        String[] statuses = {"Открыта", "Закрыта", "В работе"};
        AtomicInteger counter = new AtomicInteger(1);

        List<Task> tasks = Stream.generate(() -> new Task(
                        counter.getAndIncrement(), "Task" + counter.get(), statuses[(int) (Math.random() * statuses.length)]))
                .limit(10)
                .collect(Collectors.toList());

        tasks.forEach(task -> System.out.println(task.toString()));

        //Список задач со статусом “В работе”.
        List<Task> inWork = inWorkTasks(tasks);

        //Количество задач со статусом “Закрыта”.
        Long closed = countClosedTasks(tasks);

        //Наличие задачи с ID = 2 и отсутствие с ID = 99.
        boolean hasId = tasksHaveId(tasks, 2);
        boolean noId = tasksDoesntHaveId(tasks, 99);

        //Список задач, отсортированных по статусу (“Открыта”, “В работе”, “Закрыта”).
        List<Task> sortedTasks = sortByStatus(tasks);

        // * Объединение сначала в группы по статусам, а потом (внутри каждой
        //  группы) в подгруппы четных и нечетных по ID.
        Map<String, Map<Boolean, List<Task>>> groupedTasks = groupByStatusAndId(tasks);
//        groupedTasks.forEach((status, groupId) -> {
//            System.out.println("Статус: " + status);
//            groupId.forEach((even, taskList) ->
//            {
//                System.out.println(" " + (even ? "Четные:" : "Нечетные"));
//                taskList.forEach(task -> System.out.println("Id: " + task.getId() + " name: " + task.getName()));
//            });
//        });


        //* Разбивку на две группы: со статусом “Закрыто” и остальное.
        Map<Boolean, List<Task>> partitioned = partitionByStatus(tasks);
    }

    public static List<Task> inWorkTasks(List<Task> tasks) {
        return tasks.stream()
                .filter(t -> t.getStatus().equals("В работе"))
                .toList();
    }

    public static Long countClosedTasks(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getStatus().equals("Закрыта"))
                .count();
    }

    public static boolean tasksHaveId(List<Task> tasks, int id) {
        return tasks.stream().anyMatch(t -> t.getId() == id);
    }

    public static boolean tasksDoesntHaveId(List<Task> tasks, int id) {
        return tasks.stream().noneMatch(t -> t.getId() == id);
    }

    public static List<Task> sortByStatus(List<Task> tasks) {
        List<String> statusOrder = List.of("Открыта", "В работе", "Закрыта");
        return tasks.stream()
                .sorted((task1, task2) -> Integer.compare(statusOrder.indexOf(task1.getStatus()), statusOrder.indexOf(task2.getStatus())))
                .collect(Collectors.toList());
    }

    public static Map<String, Map<Boolean, List<Task>>> groupByStatusAndId(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.groupingBy(task -> task.getId() % 2 == 0)));
    }

    public static Map<Boolean, List<Task>> partitionByStatus(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.partitioningBy(task -> "Закрыта".equals(task.getStatus())));
    }
}