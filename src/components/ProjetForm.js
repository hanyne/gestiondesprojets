import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { FaSave, FaPlus, FaTimes } from 'react-icons/fa';

function ProjetForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [projet, setProjet] = useState({
    nom: '',
    dateDebut: '',
    dateFin: '',
    budget: '',
    statut: '',
    ressourceIds: []
  });
  const [ressources, setRessources] = useState([]);
  const [selectedRessource, setSelectedRessource] = useState('');
  const [error, setError] = useState(null);

  const statutOptions = ['En cours', 'Terminé', 'En attente', 'Annulé'];

  useEffect(() => {
    Promise.all([
      id ? fetch(`http://localhost:8080/api/projets/${id}`).then(res => res.json()) : Promise.resolve(),
      fetch('http://localhost:8080/api/ressources').then(res => res.json())
    ])
      .then(([projetData, ressourceData]) => {
        if (id && projetData) {
          setProjet({
            nom: projetData.nom || '',
            dateDebut: projetData.dateDebut || '',
            dateFin: projetData.dateFin || '',
            budget: projetData.budget || '',
            statut: projetData.statut || '',
            ressourceIds: projetData.ressources ? projetData.ressources.map(r => r.id.toString()) : []
          });
        }
        setRessources(ressourceData);
      })
      .catch(err => {
        console.error('Error fetching data:', err);
        setError(err.message);
      });
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProjet({ ...projet, [name]: value });
  };

  const handleAddRessource = () => {
    if (selectedRessource && !projet.ressourceIds.includes(selectedRessource)) {
      setProjet(prev => ({
        ...prev,
        ressourceIds: [...prev.ressourceIds, selectedRessource]
      }));
      setSelectedRessource('');
    }
  };

  const handleRemoveRessource = (idToRemove) => {
    setProjet(prev => ({
      ...prev,
      ressourceIds: prev.ressourceIds.filter(id => id !== idToRemove)
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError(null);

    // Validate required fields
    if (!projet.nom) {
      setError("Le nom du projet est requis.");
      return;
    }
    if (!projet.dateDebut) {
      setError("La date de début est requise.");
      return;
    }
    if (!projet.budget) {
      setError("Le budget est requis.");
      return;
    }

    const method = id ? 'PUT' : 'POST';
    const url = id ? `http://localhost:8080/api/projets/${id}` : 'http://localhost:8080/api/projets';
    const payload = {
      nom: projet.nom,
      dateDebut: projet.dateDebut,
      dateFin: projet.dateFin || null,
      budget: parseFloat(projet.budget),
      statut: projet.statut || "En cours",
      ressourceIds: projet.ressourceIds.map(id => parseInt(id))
    };

    console.log('Submitting payload:', payload);

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })
      .then(res => {
        if (!res.ok) {
          return res.text().then(text => { throw new Error(text || 'Erreur lors de la sauvegarde'); });
        }
        return res.json();
      })
      .then(data => {
        console.log('Projet saved:', data);
        navigate('/projets');
      })
      .catch(err => {
        console.error('Submission error:', err);
        setError(err.message || 'Erreur inattendue lors de la sauvegarde');
      });
  };

  return (
    <div className="max-w-2xl mx-auto p-6 bg-white shadow-lg rounded-lg">
      <h2 className="text-3xl font-bold mb-6 text-gray-800">{id ? 'Modifier Projet' : 'Ajouter Projet'}</h2>
      {error && <div className="text-red-500 mb-4">{error}</div>}
      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label className="block text-sm font-medium text-gray-700">Nom</label>
          <input
            type="text"
            name="nom"
            value={projet.nom}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Date de Début</label>
          <input
            type="date"
            name="dateDebut"
            value={projet.dateDebut}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Date de Fin</label>
          <input
            type="date"
            name="dateFin"
            value={projet.dateFin}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Budget</label>
          <input
            type="number"
            name="budget"
            value={projet.budget}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Statut</label>
          <select
            name="statut"
            value={projet.statut}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
          >
            <option value="">Sélectionner un statut</option>
            {statutOptions.map(option => (
              <option key={option} value={option}>{option}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Allouer une Ressource</label>
          <select
            value={selectedRessource}
            onChange={(e) => setSelectedRessource(e.target.value)}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
          >
            <option value="">Sélectionner une ressource</option>
            {ressources.map(ressource => (
              <option key={ressource.id} value={ressource.id.toString()}>
                {ressource.nom} ({ressource.type})
              </option>
            ))}
          </select>
          <button
            type="button"
            onClick={handleAddRessource}
            className="mt-2 px-3 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 flex items-center"
          >
            <FaPlus className="mr-2" /> Ajouter
          </button>
          {projet.ressourceIds.length > 0 && (
            <div className="mt-2">
              <p>Ressources sélectionnées :</p>
              <ul className="list-disc pl-5">
                {projet.ressourceIds.map(id => {
                  const ressource = ressources.find(r => r.id.toString() === id);
                  return ressource ? (
                    <li key={id} className="flex justify-between items-center">
                      {ressource.nom} ({ressource.type})
                      <button
                        type="button"
                        onClick={() => handleRemoveRessource(id)}
                        className="ml-4 text-red-500 hover:text-red-700"
                      >
                        <FaTimes />
                      </button>
                    </li>
                  ) : null;
                })}
              </ul>
            </div>
          )}
        </div>
        <button
          type="submit"
          className="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 flex items-center"
        >
          <FaSave className="mr-2" /> Sauvegarder
        </button>
      </form>
    </div>
  );
}

export default ProjetForm;