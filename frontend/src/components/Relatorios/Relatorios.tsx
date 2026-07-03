import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Relatorio4 from './Relatorio4/Relatorio4';
import './Relatorios.css';

type UserSession = {
  cpf: string;
  email: string;
};

type RelatoriosProps = {
  user: UserSession;
  onLogout: () => void;
};

const currentYear = new Date().getFullYear();
const availableYears = Array.from({ length: 5 }, (_, i) => currentYear - i).reverse();

function Relatorios({ user, onLogout }: RelatoriosProps) {
  const navigate = useNavigate();
  const [selectedYear, setSelectedYear] = useState(currentYear);
  const [menuAberto, setMenuAberto] = useState(false);

  const handleLogout = () => {
    setMenuAberto(false);
    onLogout();
    navigate('/login');
  };

  return (
    <div className="relatorios-page">
      <div className="relatorios-shell">
        <header className="relatorios-topbar">
          <div className="relatorios-brand">
            <p className="relatorios-kicker">Dashboard</p>
            <h1>Relatórios</h1>
          </div>

          <div className="relatorios-topbar-actions">
            <button type="button" className="relatorios-primary-button" onClick={() => navigate('/home')}>
              Datasets
            </button>

            <div className="relatorios-profile-menu">
              <button
                type="button"
                className="relatorios-profile-button"
                onClick={() => setMenuAberto((value) => !value)}
                aria-expanded={menuAberto}
                aria-haspopup="menu"
                title="Menu do perfil"
              >
                <span className="relatorios-profile-avatar">👤</span>
                <span className="relatorios-profile-label">
                  <strong>{user.email}</strong>
                  <small>{user.cpf}</small>
                </span>
              </button>

              {menuAberto && (
                <div className="relatorios-profile-dropdown" role="menu" aria-label="Menu do perfil">
                  <button type="button" onClick={() => navigate('/perfil')}>
                    Meu perfil
                  </button>
                  <button type="button" onClick={handleLogout}>
                    Sair da conta
                  </button>
                </div>
              )}
            </div>
          </div>
        </header>

        <main className="relatorios-content">
          <section className="relatorios-section">
            <div className="relatorios-section-header">
              <div>
                <p className="relatorios-kicker">Análise</p>
                <h2>Visualização de Relatórios</h2>
              </div>
              <div className="relatorios-year-selector">
                <label htmlFor="year-select">Selecionar ano:</label>
                <select
                  id="year-select"
                  value={selectedYear}
                  onChange={(e) => setSelectedYear(parseInt(e.target.value))}
                  className="relatorios-year-select"
                >
                  {availableYears.map((year) => (
                    <option key={year} value={year}>
                      {year}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="relatorios-container">
              <article className="relatorios-card">
                <div className="relatorios-card-header">
                  <h3>Relatório 4 - Downloads e Visualizações por mês</h3>
                  <span className="relatorios-card-badge">Análise de Acessos</span>
                </div>
                <div className="relatorios-card-content">
                  <Relatorio4 ano={selectedYear} />
                </div>
              </article>
            </div>
          </section>
        </main>
      </div>
    </div>
  );
}

export default Relatorios;
