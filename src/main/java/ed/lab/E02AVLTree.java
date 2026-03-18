package ed.lab;

import java.util.Comparator;

public class E02AVLTree<T> {

    private final Comparator<T> comparator;

    public E02AVLTree(Comparator<T> comparator) {
        this.comparator = comparator;
        this.root = null;
        this.size = 0;
    }

    private Node<T> root;
    private int size;

    public void insert(T value) {
        this.root = insert(root, value);
    }

    public void delete(T value) {
        this.root = delete(root, value);
    }

    public T search(T value) {
        Node<T> result = search(root, value);
        return result == null ? null : result.value;
    }

    public int height() {
        return getHeight(root);
    }

    public int size() {
        return size;
    }

    private Node<T> insert(Node<T> root, T value) {
        if (root == null) {
            this.size++;
            return new Node<>(value);
        }

        int compare = comparator.compare(value, root.value);

        if (compare < 0) {
            root.left = insert(root.left, value);
        } else if (compare > 0) {
            root.right = insert(root.right, value);
        } else {
            return root;
        }

        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));

        int balance = getBalance(root);

        if (balance > 1 && comparator.compare(value, root.left.value) < 0) {
            return rotateRight(root);
        }

        if (balance < -1 && comparator.compare(value, root.right.value) > 0) {
            return rotateLeft(root);
        }

        if (balance > 1 && comparator.compare(value, root.left.value) > 0) {
            root.left = rotateLeft(root.left);
            return rotateRight(root);
        }

        if (balance < -1 && comparator.compare(value, root.right.value) < 0) {
            root.right = rotateRight(root.right);
            return rotateLeft(root);
        }

        return root;
    }

    private Node<T> delete(Node<T> root, T value) {
        if (root == null) return null;

        int compare = comparator.compare(value, root.value);

        if (compare < 0) {
            root.left = delete(root.left, value);
        } else if (compare > 0) {
            root.right = delete(root.right, value);
        } else {

            if (root.left == null || root.right == null) {
                size--;
                return (root.left != null) ? root.left : root.right;
            }

            Node<T> min = root.right;
            while (min.left != null) {
                min = min.left;
            }

            root.value = min.value;
            root.right = delete(root.right, min.value);
        }

        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));

        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0) {
            return rotateRight(root);
        }

        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = rotateLeft(root.left);
            return rotateRight(root);
        }

        if (balance < -1 && getBalance(root.right) <= 0) {
            return rotateLeft(root);
        }

        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rotateRight(root.right);
            return rotateLeft(root);
        }

        return root;
    }

    private Node<T> search(Node<T> root, T value) {
        if (root == null) return null;

        int compare = comparator.compare(value, root.value);

        if (compare == 0) return root;
        if (compare < 0) return search(root.left, value);
        return search(root.right, value);
    }

    private int getHeight(Node<T> node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node<T> root) {
        if (root == null) return;
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
    }

    private int getBalance(Node<T> root) {
        if (root == null) return 0;
        return getHeight(root.left) - getHeight(root.right);
    }

    private Node<T> rotateRight(Node<T> root) {
        if (root == null || root.left == null) return root;

        Node<T> newRoot = root.left;
        root.left = newRoot.right;
        newRoot.right = root;

        updateHeight(root);
        updateHeight(newRoot);

        return newRoot;
    }

    private Node<T> rotateLeft(Node<T> root) {
        if (root == null || root.right == null) return root;

        Node<T> newRoot = root.right;
        root.right = newRoot.left;
        newRoot.left = root;

        updateHeight(root);
        updateHeight(newRoot);

        return newRoot;
    }

    private static class Node<T> {
        protected T value;
        protected Node<T> left;
        protected Node<T> right;
        protected int height;

        public Node(T value) {
            this.value = value;
            this.height = 1;
        }
    }
}