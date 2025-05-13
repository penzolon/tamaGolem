package it.unibs.fp.tamaGolem.Battaglia;

import java.util.*;
import java.util.function.Consumer;

public class TamaGolem {
    private int vita;
    private Deque<PietreElementi> listaPietre = new ArrayDeque<>();

    public TamaGolem(int vita) {
        this.listaPietre = new Deque<PietreElementi>() {
            @Override
            public void addFirst(PietreElementi pietreElementi) {
                listaPietre.addFirst(pietreElementi);
            }
            @Override
            public void addLast(PietreElementi pietreElementi) {
                listaPietre.addLast(pietreElementi);
            }
            @Override
            public boolean offerFirst(PietreElementi pietreElementi) {
                return listaPietre.offerFirst(pietreElementi);
            }
            @Override
            public boolean offerLast(PietreElementi pietreElementi) {
                return listaPietre.offerLast(pietreElementi);
            }
            @Override
            public PietreElementi removeFirst() {
                return listaPietre.removeFirst();
            }
            @Override
            public PietreElementi removeLast() {
                return listaPietre.removeLast();
            }
            @Override
            public PietreElementi pollFirst() {
                return listaPietre.pollFirst();
            }
            @Override
            public PietreElementi pollLast() {
                return listaPietre.pollLast();
            }
            @Override
            public PietreElementi getFirst() {
                return listaPietre.getFirst();
            }
            @Override
            public PietreElementi getLast() {
                return listaPietre.getLast();
            }
            @Override
            public PietreElementi peekFirst() {
                return listaPietre.peekFirst();
            }
            @Override
            public PietreElementi peekLast() {
                return listaPietre.peekLast();
            }
            @Override
            public boolean removeFirstOccurrence(Object o) {
                return listaPietre.removeFirstOccurrence(o);
            }
            @Override
            public boolean removeLastOccurrence(Object o) {
                return listaPietre.removeLastOccurrence(o);
            }
            @Override
            public boolean add(PietreElementi pietreElementi) {
                return listaPietre.add(pietreElementi);
            }
            @Override
            public boolean offer(PietreElementi pietreElementi) {
                return listaPietre.offer(pietreElementi);
            }
            @Override
            public PietreElementi remove() {
                return listaPietre.remove();
            }
            @Override
            public PietreElementi poll() {
                return listaPietre.poll();
            }
            @Override
            public PietreElementi element() {
                return listaPietre.element();
            }
            @Override
            public PietreElementi peek() {
                return listaPietre.peek();
            }
            @Override
            public boolean addAll(Collection<? extends PietreElementi> c) {
                return listaPietre.addAll(c);
            }
            @Override
            public void push(PietreElementi pietreElementi) {
                listaPietre.push(pietreElementi);
            }
            @Override
            public PietreElementi pop() {
                return listaPietre.pop();
            }
            @Override
            public boolean remove(Object o) {
                return listaPietre.remove(o);
            }
            @Override
            public boolean contains(Object o) {
                return listaPietre.contains(o);
            }
            @Override
            public int size() {
                return listaPietre.size();
            }
            @Override
            public Iterator<PietreElementi> iterator() {
                return listaPietre.iterator();
            }
            @Override
            public Iterator<PietreElementi> descendingIterator() {
                return listaPietre.descendingIterator();
            }
            @Override
            public boolean isEmpty() {
                return listaPietre.isEmpty();
            }
            @Override
            public Object[] toArray() {
                return listaPietre.toArray();
            }

            @Override
           public <T> T[] toArray(T[] a) {
                return listaPietre.toArray(a);
            }
            @Override
            public boolean containsAll(Collection<?> c) {
                return listaPietre.containsAll(c);
            }
            @Override
            public boolean removeAll(Collection<?> c) {
                return listaPietre.removeAll(c);
            }
            @Override
            public boolean retainAll(Collection<?> c) {
                return listaPietre.retainAll(c);
            }
            @Override
            public void clear() {
                listaPietre.clear();
            }
        };
        this.vita = vita;
    }

    public int getVita() {
        return vita;
    }

    public Deque<PietreElementi> getListaPietre() {
        return listaPietre;
    }
}