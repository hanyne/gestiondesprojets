import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

function FinancialReport() {
  const [projets, setProjets] = useState([]);
  const [totalCosts, setTotalCosts] = useState({});
  const [resourceUsage, setResourceUsage] = useState({});
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    fetch('http://localhost:8080/api/projets')
      .then(res => res.json())
      .then(data => {
        setProjets(data);
        data.forEach(projet => {
          fetch(`http://localhost:8080/api/projets/${projet.id}/cout-total`)
            .then(res => res.json())
            .then(cost => setTotalCosts(prev => ({ ...prev, [projet.id]: cost })));
          fetch(`http://localhost:8080/api/projets/${projet.id}/ressources`)
            .then(res => res.json())
            .then(resources => setResourceUsage(prev => ({ ...prev, [projet.id]: resources.length })));
        });
      })
      .catch(err => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="max-w-4xl mx-auto p-6 bg-white shadow-lg rounded-lg">
      <h2 className="text-3xl font-bold mb-6 text-gray-800">Rapport Financier Avancé</h2>
      {error && <div className="text-red-500 mb-4">{error}</div>}
      {loading && <div className="text-gray-500">Chargement...</div>}
      <ul className="space-y-4">
        {projets.map(projet => (
          <li key={projet.id} className="border p-4 rounded-md">
            <div className="font-medium">{projet.nom} - {projet.statut}</div>
            <div>Budget: {projet.budget}€</div>
            <div>Coût Total: {totalCosts[projet.id] || 'Chargement...'}€</div>
            <div>Écart: {(projet.budget - (totalCosts[projet.id] || 0)).toFixed(2)}€</div>
            <div>Ressources Utilisées: {resourceUsage[projet.id] || 0}</div>
          </li>
        ))}
      </ul>
      <Link to="/projets" className="mt-4 inline-block px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600">
        Retour aux Projets
      </Link>
    </div>
  );
}

export default FinancialReport;