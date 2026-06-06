import React from 'react';
import { useNavigate } from 'react-router-dom';

type UserSession = {
  cpf: string;
  email: string;
};

type HomeProps = {
  user: UserSession;
  onLogout: () => void;
};

function Home({ user, onLogout }: HomeProps) {
  const navigate = useNavigate();

  const handleLogout = () => {
    onLogout();
    navigate('/login');
  };

  return (
    <div className="container">
      <div className="card" style={{ position: 'relative' }}>
        <div 
          onClick={() => navigate('/perfil')}
          style={{ 
            position: 'absolute', 
            top: '20px', 
            right: '20px', 
            cursor: 'pointer', 
            fontSize: '1.5rem' 
          }}
          title="Editar Perfil"
        >
          👤
        </div>
        <h1>Tela Inicial</h1>
        <p>Bem-vindo!</p>
        <p>
          CPF: {user.cpf}
          <br />
          Email: {user.email}
        </p>
        <button onClick={handleLogout}>Sair</button>
      </div>
    </div>
  );
}

export default Home;
