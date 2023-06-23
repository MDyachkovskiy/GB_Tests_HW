package com.geekbrains.tests

import com.geekbrains.tests.presenter.details.DetailsPresenter
import com.geekbrains.tests.view.details.ViewDetailsContract
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class DetailsPresenterTest {

    private lateinit var presenter: DetailsPresenter

    @Mock
    private lateinit var viewContract: ViewDetailsContract

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        presenter = DetailsPresenter(0)
        presenter.onAttach(viewContract)
    }

    @Test
    fun testSetCounter() {
        presenter.setCounter(10)
        presenter.onIncrement()
        verify(viewContract).setCount(11)
    }

    @Test
    fun testOnIncrement() {
        presenter.onIncrement()
        verify(viewContract).setCount(1)
    }

    @Test
    fun testOnDecrement() {
        presenter.setCounter(10)
        presenter.onDecrement()
        verify(viewContract).setCount(9)
    }

    @Test
    fun testOnAttachDetach() {
        presenter.onDetach()
        presenter.onIncrement()
        verify(viewContract, never()).setCount(anyInt())
    }

}