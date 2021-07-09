package com.lyric.android.app.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 数组工具类
 *
 * @author Lyric Gan
 * @since 2020/12/22
 */
public class ArraysHelper {

    public static class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public static int binarySearch(int[] array, int fromIndex, int toIndex, int value) {
        if (array == null || array.length == 0) {
            return -1;
        }
        if (fromIndex > toIndex || fromIndex < 0 || toIndex > array.length) {
            return -1;
        }
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            final int middle = (low + high) >>> 1;
            final int middleValue = array[middle];
            if (middleValue < value) {
                low = middle + 1;
            } else if (middleValue > value) {
                high = middle - 1;
            } else {
                return middle;
            }
        }
        return -(low + 1);
    }

    public static int[] bubbleSort(int[] array) {
        if (array == null || array.length == 0) {
            return array;
        }
        int length = array.length;
        int temp;
        for (int i = length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array;
    }

    public static int[] bubbleSort(int[] array, int min, int max) {
        if (array == null || array.length == 0) {
            return array;
        }
        int i;
        int temp;
        while (min < max) {
            for (i = min; i < max; i++) {
                if (array[i] > array[i + 1]) {
                    temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                }
            }
            max--;
            for (i = max; i > min; i--) {
                if (array[i] < array[i - 1]) {
                    temp = array[i];
                    array[i] = array[i - 1];
                    array[i - 1] = temp;
                }
            }
            min++;
        }
        return array;
    }

    public static int[] quickSort(int[] array, int low, int high) {
        if (array == null || array.length == 0) {
            return array;
        }
        quickSortInner(array, low, high);
        return array;
    }

    private static void quickSortInner(int[] array, int low, int high) {
        if (low < high) {
            int middle = partition(array, low, high);
            quickSortInner(array, low, middle - 1);
            quickSortInner(array, middle + 1, high);
        }
    }

    private static int partition(int[] array, int low, int high) {
        if (array == null || array.length == 0) {
            return 0;
        }
        int i = low - 1;
        int j;
        int temp;
        for (j = low; j < high; ++j) {
            if (array[j] < array[high]) {
                temp = array[++i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return (i + 1);
    }

    public static ListNode buildList() {
        ListNode node1 = new ListNode(1, null);
        ListNode node2 = new ListNode(2, node1);
        ListNode node3 = new ListNode(3, node2);
        ListNode node4 = new ListNode(4, node3);
        ListNode node5 = new ListNode(5, node4);
        ListNode node6 = new ListNode(6, node5);
        ListNode node7 = new ListNode(7, node6);
        ListNode node8 = new ListNode(8, node7);
        return new ListNode(9, node8);
    }

    public static TreeNode buildTreeNode() {
        TreeNode leftNode1 = new TreeNode(1, null, null);
        TreeNode rightNode1 = new TreeNode(3, null, null);
        TreeNode leftNode2 = new TreeNode(8, null, null);
        TreeNode rightNode2 = new TreeNode(13, null, null);
        TreeNode leftNode3 = new TreeNode(2, leftNode1, rightNode1);
        TreeNode rightNode3 = new TreeNode(9, leftNode2, rightNode2);
        return new TreeNode(6, leftNode3, rightNode3);
    }

    public static void main(String[] args) {
        ListNode head = buildList();

        printList(head);
        ListNode newNode = reverseList(head);
        printList(newNode);

        ListNode head2 = buildList();
        ListNode newNode2 = reorderList(head2);
        printList(newNode2);

        ListNode head3 = buildList();
        ListNode newNode3 = reverseList(head3, 2, 5);
        printList(newNode3);

        System.out.println("============================");

        TreeNode root = buildTreeNode();
        preorderTraversal(root);
        System.out.println();
        inorderTraversal(root);
        System.out.println();
        postorderTraversal(root);
        System.out.println();

        List<List<Integer>> resultItems = levelTraversal(root);
        System.out.println(resultItems.toString());

        resultItems = levelTraversalZ(root);
        System.out.println(resultItems.toString());

        resultItems = levelTraversalReverse(root);
        System.out.println(resultItems.toString());

        System.out.println("============================");

        Storage storage = new Storage();
        Producer producer = new Producer(storage);
        Consumer consumer = new Consumer(storage);

        for (int i = 0; i < 50; i++) {
            new Thread(producer, "生产者" + (i + 1)).start();
            new Thread(consumer, "消费者" + (i + 1)).start();
        }
    }

    public static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode curr = head;
        ListNode prev = null;
        while (curr.next != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        curr.next = prev;
        return curr;
    }

    public static void reverseListNoReturn(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
    }

    public static ListNode reorderList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode midNode = getMidNode(head);
        ListNode next = midNode.next;
        midNode.next = null;
        ListNode node2 = reverseList(next);
        printList(head);
        printList(node2);
        return mergeIntervalNode(head, node2);
    }

    public static ListNode getMidNode(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    public static ListNode mergeIntervalNode(ListNode node1, ListNode node2) {
        ListNode curr1 = node1;
        ListNode curr2 = node2;
        while (curr1 != null && curr2 != null) {
            ListNode next1 = curr1.next;
            curr1.next = curr2;
            curr1 = next1;

            ListNode next2 = curr2.next;
            curr2.next = next1;
            curr2 = next2;
        }
        return node1;
    }

    public static ListNode reverseList(ListNode head, int left, int right) {
        if (head == null || left > right) {
            return head;
        }
        ListNode prev = head;
        for (int i = 0; i < left - 1; i++) {
            prev = prev.next;
        }
        ListNode post = prev;
        for (int i = 0; i < right - left + 1; i++) {
            post = post.next;
        }
        ListNode leftNode = prev.next;
        ListNode rightNode = post.next;

        prev.next = null;
        post.next = null;

        reverseListNoReturn(leftNode);

        prev.next = post;
        leftNode.next = rightNode;

        return head;
    }

    public static void printList(ListNode head) {
        ListNode curr = head;
        while (curr != null) {
            System.out.print(curr.val + " ");
            curr = curr.next;
        }
        System.out.println();
    }

    public static void preorderTraversal(TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.print(root.val + " ");
        preorderTraversal(root.left);
        preorderTraversal(root.right);
    }

    public static void inorderTraversal(TreeNode root) {
        if (root == null) {
            return;
        }
        inorderTraversal(root.left);
        System.out.print(root.val + " ");
        inorderTraversal(root.right);
    }

    public static void postorderTraversal(TreeNode root) {
        if (root == null) {
            return;
        }
        postorderTraversal(root.left);
        postorderTraversal(root.right);
        System.out.print(root.val + " ");
    }

    public static List<List<Integer>> levelTraversal(TreeNode root) {
        List<List<Integer>> resultItems = new ArrayList<>();
        if (root == null) {
            return resultItems;
        }
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            List<Integer> items = new ArrayList<>();
            for (int i = 0, size = queue.size(); i < size; i++) {
                TreeNode node = queue.pop();
                items.add(node.val);
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            resultItems.add(items);
        }
        return resultItems;
    }

    public static List<List<Integer>> levelTraversalZ(TreeNode root) {
        List<List<Integer>> resultItems = new LinkedList<>();
        if (root == null) {
            return resultItems;
        }
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        boolean isLeftOrder = false;
        while (!queue.isEmpty()) {
            LinkedList<Integer> items = new LinkedList<>();
            for (int i = 0, size = queue.size(); i < size; i++) {
                TreeNode node = queue.pop();
                if (isLeftOrder) {
                    items.add(node.val);
                } else {
                    items.push(node.val);
                }
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            resultItems.add(items);
            isLeftOrder = !isLeftOrder;
        }
        return resultItems;
    }

    public static List<List<Integer>> levelTraversalReverse(TreeNode root) {
        List<List<Integer>> resultItems = new LinkedList<>();
        if (root == null) {
            return resultItems;
        }
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            List<Integer> items = new ArrayList<>();
            for (int i = 0, size = queue.size(); i < size; i++) {
                TreeNode node = queue.pop();
                items.add(node.val);
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            resultItems.add(0, items);
        }
        return resultItems;
    }

    public static class Storage {
        private final LinkedList<Integer> queue = new LinkedList<>();
        private static final int MAX_COUNT = 5;

        public void produce() {
            synchronized (this) {
                while (queue.size() == MAX_COUNT) {
                    System.out.println(Thread.currentThread().getName() + " 仓库已满，等待消费。仓库里剩余数量为 " + queue.size());
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                queue.add(1);
                System.out.println(Thread.currentThread().getName() + " 生产了一件商品，仓库里剩余数量为 " + queue.size());
                notifyAll();
            }
        }

        public void consume() {
            synchronized (this) {
                while (queue.isEmpty()) {
                    System.out.println(Thread.currentThread().getName() + " 仓库空了，等待生产。仓库里剩余数量为 " + queue.size());
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                queue.poll();
                System.out.println(Thread.currentThread().getName() + " 消费了一件商品，仓库里剩余数量为 " + queue.size());
                notifyAll();
            }
        }
    }

    public static class Producer implements Runnable {
        private final Storage mStorage;

        public Producer(Storage storage) {
            this.mStorage = storage;
        }

        @Override
        public void run() {
            if (mStorage != null) {
                mStorage.produce();
            }
        }
    }

    public static class Consumer implements Runnable {
        private final Storage mStorage;

        public Consumer(Storage storage) {
            this.mStorage = storage;
        }

        @Override
        public void run() {
            if (mStorage != null) {
                mStorage.consume();
            }
        }
    }
}
