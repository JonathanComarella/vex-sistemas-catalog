import './styles.scss';
import { Switch, Route } from 'react-router-dom';
import Login from './components/Login';

const Auth = () => (
  <div className="auth-container">

    <div className="auth-content">
      <Switch>
        <Route path="/auth/login">
          <Login/>
        </Route>
        <Route path="/auth/register">
          <h1>Cadastro</h1>
        </Route>
        <Route path="/auth/recover">
          <h1>Recuperação</h1>
        </Route>
      </Switch>
    </div>
  </div>
);

export default Auth;