package utils

import arrow.core.Option
import arrow.core.firstOrNone
import arrow.core.lastOrNone
import arrow.core.none
import arrow.core.toOption

class Queue<T>(private val elements: List<T> = emptyList()) {

	companion object {
		fun <T> from(list: List<T>) = Queue(list)
	}

	fun asList(): List<T> = elements

	// Add an element to the queue (enqueue), returns a new Queue with the element added
	fun enqueue(item: T): Queue<T> = Queue(elements + item)

	// Remove an element from the queue (dequeue), returns a new Queue and the dequeued element wrapped in Option
	fun dequeue(): Pair<Queue<T>, Option<T>> =
		if (isEmpty()) {
			Pair(this, none())
		} else {
			Pair(Queue(elements.drop(1)), elements.firstOrNone())
		}

	// Check if the queue is empty
	fun isEmpty(): Boolean = elements.isEmpty()

	// Peek at the front element without removing it
	fun peek(): Option<T> = elements.firstOrNone()

	fun size(): Int = elements.size

	override fun toString(): String = elements.toString()
}

class Stack<T>(private val elements: List<T> = emptyList()) {

	companion object {
		fun <T> from(list: List<T>) = Stack(list)
	}

	fun asList(): List<T> = elements

	// Push an element onto the stack, returns a new Stack with the element added
	fun push(item: T): Stack<T> = Stack(elements + item)

	fun push(items: List<T>): Stack<T> = Stack(elements + items)

	// Pop an element from the stack, returns a new Stack and the popped element wrapped in Option
	fun pop(): Pair<Stack<T>, Option<T>> =
		if (isEmpty()) {
			Pair(this, none())
		} else {
			Pair(Stack(elements.dropLast(1)), elements.lastOrNone())
		}

	// Peek at the top element without removing it
	fun peek(): Option<T> = elements.lastOrNone()

	// Check if the stack is empty
	fun isEmpty(): Boolean = elements.isEmpty()

	fun size(): Int = elements.size

	override fun toString(): String = elements.toString()
}

fun <L, R> Pair<L, R>.runLeft(fn: (first: L, second: R) -> Unit): R {
	fn(first, second)
	return this.second
}

class MutableQueue<T>(list: List<T> = listOf()) {
	private val elements: MutableList<T> = list.toMutableList()

	fun asList() = elements.toList()

	// Add an element to the queue (enqueue)
	fun enqueue(item: T) = elements.add(item)

	// Remove an element from the queue (dequeue)
	fun dequeue(): Option<T> =
		if (isEmpty()) {
			none()
		} else elements.removeAt(0).toOption()


	// Check if the queue is empty
	fun isEmpty(): Boolean = elements.isEmpty()

	// Peek at the front element without removing it
	fun peek(): Option<T> = elements.firstOrNone()

	fun size(): Int = elements.size

	override fun toString(): String = elements.toString()
}

class MutableStack<T>(list: List<T> = listOf()) {

	private val elements: MutableList<T> = list.toMutableList()

	fun asList() = elements.toList()

	// Push an element onto the stack
	fun push(item: T) {
		elements.add(item)
	}

	// Pop an element from the stack
	fun pop(): Option<T> =
		if (isEmpty()) {
			none()
		} else elements.removeAt(elements.size - 1).toOption()


	// Peek at the top element without removing it
	fun peek(): Option<T> = elements.lastOrNone()


	fun isEmpty(): Boolean = elements.isEmpty()


	fun size(): Int = elements.size

	override fun toString(): String = elements.toString()

}