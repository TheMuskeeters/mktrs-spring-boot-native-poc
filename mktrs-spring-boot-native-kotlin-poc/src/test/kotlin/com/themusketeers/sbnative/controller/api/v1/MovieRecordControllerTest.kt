/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDCONTROLLERTEST.KT                                */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.16/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.controller.api.v1

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

/**
 * Unit test for checking {@link MovieRecordController} api.
 * This does not make the call to the real web server.
 * <p><b>Path:</b>{@code api/v1/movie/records}</p>
 * <p>Some reference links follow:
 * <ul>
 *     <li><a href="https://rieckpil.de/test-your-spring-mvc-controller-with-webtestclient-against-mockmvc/">Test Your Spring MVC Controller with the WebTestClient and MockMvc</a></li>
 *     <li><a href="https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/">Spring 5 WebClient and WebTestClient Tutorial with Examples</a></li>
 * </ul>
 * </p>
 *
 * @author COQ- Carlos Adolfo Ortiz Q.
 */
@WebMvcTest(MovieRecordController::class)
class MovieRecordControllerTest
