package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import javafx.scene.text.Text;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//@RunWith(PowerMockRunner.class)
@ExtendWith(ApplicationExtension.class)
@PrepareForTest(LoginCtrl.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginCtrlTest {

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;
    LoginCtrl loginCtrl;


    @Start
    public void init(Stage stage) throws IOException {
        // mock using Mockito
        mainCtrl = mock(MainCtrl.class);
        // mock using Powermock
        serverUtils = PowerMockito.mock(ServerUtils.class);

        loginCtrl = new LoginCtrl(mainCtrl, serverUtils);
    }

    @Test
    public void loginTest() throws Exception {
        // for pipeline issue
        WaitForAsyncUtils.clearExceptions();
        //fill in dummy data for 'error' Text
        Whitebox.setInternalState(loginCtrl, "error", new Text("mock error message"));

        String username = "Joe";
        String password = "password";
        PowerMockito.when(serverUtils.getToken(username, password)).thenReturn("mock token");
        loginCtrl.login(username, password);

        assertEquals("mock token", Main.TOKEN);
        assertEquals(username, Main.USERNAME);
        // verify serverUtil is invoked
        PowerMockito.verifyPrivate(serverUtils, times(1)).invoke("getToken", username, password);
    }

    @Test
    public void showRegisterTest() {
        loginCtrl.showRegister();
        verify(mainCtrl, times(1)).showScene(RegisterCtrl.class);
    }

    @Test
    public void onLoginButtonPressedTest() {
        // fill the text fields
        Whitebox.setInternalState(loginCtrl, "userName", new TextField("Kim"));
        Whitebox.setInternalState(loginCtrl, "password", new TextField("p"));
        LoginCtrl loginSpy = spy(loginCtrl);

        doNothing().when(loginSpy).login(any(), any());

        loginSpy.onLoginButtonPressed();

        verify(loginSpy, times(1)).login(any(), any());
    }

    @Test
    public void showConnectionTest() {
        loginCtrl.showConnection();

        verify(mainCtrl, times(1)).showScene(ConnectionCtrl.class);
    }
}
