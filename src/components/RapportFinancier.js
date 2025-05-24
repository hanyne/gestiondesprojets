import React, { useEffect, useState } from 'react';
import { FaChartBar } from 'react-icons/fa';

function RapportFinancier() {
  const [rapports, setRapports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        setError(null);

        // Fetch tasks
        const response = await fetch('http://localhost:8080/api/taches');
        if (!response.ok) {
          throw new Error('Erreur lors de la récupération des tâches');
        }
        const taches = await response.json();

        // Map tasks to financial reports
        const rapportsData = taches.map(tache => {
          const totalCout = tache.ressources
            ? tache.ressources.reduce((sum, ressource) => sum + (ressource.cout || 0), 0)
            : 0;

          return {
            tacheId: tache.id,
            tacheNom: tache.nom,
            projetNom: tache.projet ? tache.projet.nom : 'N/A',
            budgetProjet: tache.projet ? (tache.projet.budget || 0) : 0,
            totalCoutRessources: totalCout,
            coutRestant: tache.projet ? (tache.projet.budget || 0) - totalCout : 0,
            deadline: tache.deadline || 'N/A'
          };
        });

        setRapports(rapportsData);
      } catch (err) {
        console.error('Erreur:', err);
        setError(err.message || 'Une erreur est survenue lors du chargement des rapports');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return (
      <div className="container mx-auto p-6">
        <h2 className="text-3xl font-bold mb-6 text-gray-800 flex items-center">
          <FaChartBar className="mr-2" /> Rapports Financiers
        </h2>
        <p>Chargement des données...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container mx-auto p-6">
        <h2 className="text-3xl font-bold mb-6 text-gray-800 flex items-center">
          <FaChartBar className="mr-2" /> Rapports Financiers
        </h2>
        <p className="text-red-500">{error}</p>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-6">
      <h2 className="text-3xl font-bold mb-6 text-gray-800 flex items-center">
        <FaChartBar className="mr-2" /> Rapports Financiers
      </h2>
      {rapports.length === 0 ? (
        <p>Aucun rapport financier disponible.</p>
      ) : (
        <div className="overflow-x-auto">
          <table className="min-w-full bg-white shadow-md rounded-lg overflow-hidden">
            <thead className="bg-gray-800 text-white">
              <tr>
                <th className="py-3 px-4 text-left">Tache ID</th>
                <th className="py-3 px-4 text-left">Nom Tache</th>
                <th className="py-3 px-4 text-left">Projet</th>
                <th className="py-3 px-4 text-left">Budget Projet</th>
                <th className="py-3 px-4 text-left">Coût Total Ressources</th>
                <th className="py-3 px-4 text-left">Coût Restant</th>
                <th className="py-3 px-4 text-left">Deadline</th>
              </tr>
            </thead>
            <tbody>
              {rapports.map(rapport => (
                <tr key={rapport.tacheId} className="border-b hover:bg-gray-100">
                  <td className="py-3 px-4">{rapport.tacheId}</td>
                  <td className="py-3 px-4">{rapport.tacheNom}</td>
                  <td className="py-3 px-4">{rapport.projetNom}</td>
                  <td className="py-3 px-4">{rapport.budgetProjet.toFixed(2)} €</td>
                  <td className="py-3 px-4">{rapport.totalCoutRessources.toFixed(2)} €</td>
                  <td className="py-3 px-4">{rapport.coutRestant.toFixed(2)} €</td>
                  <td className="py-3 px-4">{rapport.deadline}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default RapportFinancier;