package com.lyric.android.app.test.arithmetic;

import java.util.LinkedList;

/**
 * 二叉树：是每个结点最多有两个子树的有序树，在使用二叉树的时候，数据并不是随便插入到节点中的，
 * 一个节点的左子节点的关键值必须小于此节点，右子节点的关键值必须大于或者是等于此节点，所以又称二叉查找树、二叉排序树、二叉搜索树。
 *
 * 完全二叉树：若设二叉树的高度为h，除第 h 层外，其它各层 (1～h-1) 的结点数都达到最大个数，第h层有叶子结点，
 * 并且叶子结点都是从左到右依次排布，这就是完全二叉树。
 *
 * 满二叉树：除了叶结点外每一个结点都有左右子叶且叶子结点都处在最底层的二叉树。
 *
 * 深度：二叉树的层数，就是深度。
 *
 * @author lyricgan
 */
public class BinaryTree {
    // 根结点
    private Node<Integer> rootNode;
    // 二叉树中结点数量
    private int size;

    public BinaryTree() {
        rootNode = new Node<>();
    }

    public BinaryTree(int[] values) {
        System.out.print("新建binaryTree:");
        for (int i : values) {
            System.out.print(i);
        }
        System.out.println();
        boolean isLeft = true;
        int len = values.length;
        if (len == 0)
            return;
        LinkedList<Node<Integer>> queue = new LinkedList<Node<Integer>>();
        rootNode = new Node<Integer>(values[0]);
        queue.addLast(rootNode);
        Node<Integer> parent = null;
        Node<Integer> current = null;
        for (int i = 1; i < len; i++) {
            current = new Node<Integer>(values[i]);
            queue.addLast(current);
            if (isLeft) {
                parent = queue.getFirst();
            } else {
                parent = queue.removeFirst();
            }
            if (isLeft) {
                parent.setLeftChild(current);
                isLeft = false;
            } else {
                parent.setRightChild(current);
                isLeft = true;
            }
        }
    }

    // 递归中序遍历
    public void inorder() {
        System.out.print("binaryTree递归中序遍历:");
        inorderTraverseRecursion(rootNode);
        System.out.println();
    }

    // 层次遍历
    public void layerOrder() {
        System.out.print("binaryTree层次遍历:");
        LinkedList<Node<Integer>> queue = new LinkedList<Node<Integer>>();
        queue.addLast(rootNode);
        Node<Integer> current = null;
        while (!queue.isEmpty()) {
            current = queue.removeFirst();
            if (current.getLeftChild() != null) {
                queue.addLast(current.getLeftChild());
            }
            if (current.getRightChild() != null) {
                queue.addLast(current.getRightChild());
            }
            System.out.print(current.getValue());
        }
        System.out.println();
    }

    // 获得二叉树深度
    public int getDepth() {
        return getDepthRecursion(rootNode);
    }

    private int getDepthRecursion(Node<Integer> node) {
        if (node == null)
            return 0;
        int llen = getDepthRecursion(node.getLeftChild());
        int rlen = getDepthRecursion(node.getRightChild());
        int maxlen = Math.max(llen, rlen);
        return maxlen + 1;
    }

    // 递归先序遍历
    public void preorder() {
        System.out.print("binaryTree递归先序遍历:");
        preorderTraverseRecursion(rootNode);
        System.out.println();
    }

    private void inorderTraverseRecursion(Node<Integer> node) {
        if (node.getLeftChild() != null) {
            inorderTraverseRecursion(node.getLeftChild());
        }
        System.out.print(node.getValue());
        if (node.getRightChild() != null) {
            inorderTraverseRecursion(node.getRightChild());
        }
    }

    private void preorderTraverseRecursion(Node<Integer> node) {
        System.out.print(node.getValue());
        if (node.getLeftChild() != null) {
            preorderTraverseRecursion(node.getLeftChild());
        }
        if (node.getRightChild() != null) {
            preorderTraverseRecursion(node.getRightChild());
        }
    }

    // 非递归先序遍历
    public void preorderNoRecursion() {
        System.out.print("binaryTree非递归先序遍历:");
        LinkedList<Node<Integer>> stack = new LinkedList<Node<Integer>>();
        stack.push(rootNode);
        Node<Integer> current = null;
        while (!stack.isEmpty()) {
            current = stack.pop();
            System.out.print(current.getValue());
            if (current.getRightChild() != null)
                stack.push(current.getRightChild());
            if (current.getLeftChild() != null)
                stack.push(current.getLeftChild());
        }
        System.out.println();
    }

    /**
     * 非递归中序遍历
     * 栈内保存将要访问的元素
     */
    public void inorderNoRecursion() {
        System.out.print("binaryTree非递归中序遍历:");
        LinkedList<Node<Integer>> stack = new LinkedList<Node<Integer>>();
        Node<Integer> current = rootNode;
        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.getLeftChild();
            }
            if (!stack.isEmpty()) {
                current = stack.pop();
                System.out.print(current.getValue());
                current = current.getRightChild();
            }
        }
        System.out.println();
    }

    /**
     * 非递归后序遍历
     * <p>
     * 当上一个访问的结点是右孩子或者当前结点没有右孩子则访问当前结点
     */
    public void postorderNoRecursion() {
        System.out.print("binaryTree非递归后序遍历:");
        Node<Integer> rNode = null;
        Node<Integer> current = rootNode;
        LinkedList<Node<Integer>> stack = new LinkedList<Node<Integer>>();
        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.getLeftChild();
            }
            current = stack.pop();
            while (current != null && (current.getRightChild() == null || current.getRightChild() == rNode)) {
                System.out.print(current.getValue());
                rNode = current;
                if (stack.isEmpty()) {
                    System.out.println();
                    return;
                }
                current = stack.pop();
            }
            stack.push(current);
            if (current != null) {
                current = current.getRightChild();
            }
        }
    }

    public static void main(String[] args) {
        BinaryTree binaryTree = new BinaryTree(new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        binaryTree.inorder();
        binaryTree.preorder();
        binaryTree.layerOrder();
        binaryTree.preorderNoRecursion();
        binaryTree.inorderNoRecursion();
        binaryTree.postorderNoRecursion();
        System.out.println("深度为：" + binaryTree.getDepth());
    }

    /**
     * 二叉树结构实体
     * @param <V>
     */
    private static class Node<V> {
        /** 节点数值 */
        private V value;
        /** 左子节点 */
        private Node<V> leftChild;
        /** 右子节点 */
        private Node<V> rightChild;

        public Node() {
        }

        public Node(V value) {
            this.value = value;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<V> getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(Node<V> leftChild) {
            this.leftChild = leftChild;
        }

        public Node<V> getRightChild() {
            return rightChild;
        }

        public void setRightChild(Node<V> rightChild) {
            this.rightChild = rightChild;
        }
    }
}
