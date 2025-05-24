import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import ProjetList from './components/ProjetList';
import ProjetForm from './components/ProjetForm';
import TacheList from './components/TacheList';
import TacheForm from './components/TacheForm';
import RessourceList from './components/RessourceList';
import RessourceForm from './components/RessourceForm';
import EmployeList from './components/EmployeList';
import EmployeForm from './components/EmployeForm';
import FinancialReport from './components/FinancialReport';
import './App.css';
import RapportFinancier from './components/RapportFinancier';

function App() {
  return (
    <Router>
      <div className="app-container">
        <header className="header">
          <h1 className="title">Gestion des Projets</h1>
          <nav className="nav">
            <Link to="/projets" className="nav-link">Projets</Link>
            <Link to="/taches" className="nav-link">Tâches</Link>
            <Link to="/ressources" className="nav-link">Ressources</Link>
            <Link to="/employes" className="nav-link">Employés</Link>
            <Link to="/financial-report" className="nav-link">Rapport Financier</Link>
          </nav>
        </header>
        <main className="main-content">
          <Routes>
            <Route path="/projets" element={<ProjetList />} />
            
            <Route path="/projets/edit/:id" element={<ProjetForm />} />
            <Route path="/taches" element={<TacheList />} />
            
            <Route path="/taches/edit/:id" element={<TacheForm />} />
            <Route path="/ressources" element={<RessourceList />} />
            <Route path="/ressources/new" element={<RessourceForm />} />
            <Route path="/ressources/edit/:id" element={<RessourceForm />} />
            <Route path="/employes" element={<EmployeList />} />
            <Route path="/employes/new" element={<EmployeForm />} />
            <Route path="/employes/edit/:id" element={<EmployeForm />} />
            <Route path="/financial-report" element={<RapportFinancier />} />
            <Route path="/projets/add" element={<ProjetForm />} />
<Route path="/taches/add" element={<TacheForm />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;