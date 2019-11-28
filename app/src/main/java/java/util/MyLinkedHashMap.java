package java.util;

/*
 *  @项目名：  Componentization
 *  @包名：    java.util
 *  @文件名:   MyLinkedHashMapEntry
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/21 2:19 PM
 *  @描述：    TODO
 */
public class MyLinkedHashMap<K, V> extends MyHashMap<K, V> implements Map<K, V> {

    transient LinkedHashMapEntry<K,V> head;


    transient LinkedHashMapEntry<K,V> tail;


    final boolean accessOrder;

    public MyLinkedHashMap() {
        super();
        accessOrder = false;
    }

    public MyLinkedHashMap(int initialCapacity) {
        super(initialCapacity);
        accessOrder = false;
    }

    public MyLinkedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        accessOrder = false;
    }

    static class LinkedHashMapEntry<K, V> extends MyHashMap.Node<K, V> {
        LinkedHashMapEntry<K, V> before, after;

        LinkedHashMapEntry(int hash, K key, V value, Node<K, V> next) {
            super(hash, key, value, next);
        }
    }

    Node<K,V> newNode(int hash, K key, V value, Node<K,V> e) {
        LinkedHashMapEntry<K,V> p = new LinkedHashMapEntry<K,V>(hash, key, value, e);
        linkNodeLast(p);
        return p;
    }

    private void linkNodeLast(LinkedHashMapEntry<K,V> p) {
        LinkedHashMapEntry<K,V> last = tail;
        tail = p;
        if (last == null)
            head = p;
        else {
            p.before = last;
            last.after = p;
        }
    }

    Node<K,V> replacementNode(Node<K,V> p, Node<K,V> next) {
        LinkedHashMapEntry<K,V> q = (LinkedHashMapEntry<K,V>)p;
        LinkedHashMapEntry<K,V> t =
                new LinkedHashMapEntry<K,V>(q.hash, q.key, q.value, next);
        transferLinks(q, t);
        return t;
    }

    TreeNode<K,V> replacementTreeNode(Node<K,V> p, Node<K,V> next) {
        LinkedHashMapEntry<K,V> q = (LinkedHashMapEntry<K,V>)p;
        TreeNode<K,V> t = new TreeNode<K,V>(q.hash, q.key, q.value, next);
        transferLinks(q, t);
        return t;
    }

    private void transferLinks(LinkedHashMapEntry<K,V> src,
                               LinkedHashMapEntry<K,V> dst) {
        LinkedHashMapEntry<K,V> b = dst.before = src.before;
        LinkedHashMapEntry<K,V> a = dst.after = src.after;
        if (b == null)
            head = dst;
        else
            b.after = dst;
        if (a == null)
            tail = dst;
        else
            a.before = dst;
    }

    void reinitialize() {
        super.reinitialize();
        head = tail = null;
    }

    void afterNodeRemoval(Node<K,V> e) { // unlink
        LinkedHashMapEntry<K,V> p =
                (LinkedHashMapEntry<K,V>)e, b = p.before, a = p.after;
        p.before = p.after = null;
        if (b == null)
            head = a;
        else
            b.after = a;
        if (a == null)
            tail = b;
        else
            a.before = b;
    }



    void afterNodeAccess(Node<K,V> e) { // move node to last
        LinkedHashMapEntry<K,V> last;
        if (accessOrder && (last = tail) != e) {
            LinkedHashMapEntry<K,V> p =
                    (LinkedHashMapEntry<K,V>)e, b = p.before, a = p.after;
            p.after = null;
            if (b == null)
                head = a;
            else
                b.after = a;
            if (a != null)
                a.before = b;
            else
                last = b;
            if (last == null)
                head = p;
            else {
                p.before = last;
                last.after = p;
            }
            tail = p;
            ++modCount;
        }
    }
    void afterNodeInsertion(boolean evict) { // possibly remove eldest
        LinkedHashMapEntry<K,V> first;
        if (evict && (first = head) != null && removeEldestEntry(first)) {
            K key = first.key;
            removeNode(hash(key), key, null, false, true);
        }
    }
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return false;
    }

}
