import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { FaSave, FaPlus, FaTimes } from 'react-icons/fa';

function TacheForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [tache, setTache] = useState({
    nom: '',
    projetId: '',
    responsableId: '',
    description: '',
    etat: '',
    priorite: '',
    deadline: '',
    ressourceIds: []
  });
  const [projets, setProjets] = useState([]);
  const [employes, setEmployes] = useState([]);
  const [ressources, setRessources] = useState([]);
  const [selectedRessource, setSelectedRessource] = useState('');
  const [error, setError] = useState(null);

  const etatOptions = ['En cours', 'Terminée', 'En attente', 'Annulée'];
  const prioriteOptions = ['Basse', 'Moyenne', 'Haute', 'Critique'];

  useEffect(() => {
    Promise.all([
      id ? fetch(`http://localhost:8080/api/taches/${id}`).then(res => res.json()) : Promise.resolve(),
      fetch('http://localhost:8080/api/projets').then(res => res.json()),
      fetch('http://localhost:8080/api/employes').then(res => res.json()),
      fetch('http://localhost:8080/api/ressources').then(res => res.json())
    ])
      .then(([tacheData, projetData, employeData, ressourceData]) => {
        if (id && tacheData) {
          setTache({
            nom: tacheData.nom || '',
            projetId: tacheData.projet ? tacheData.projet.id.toString() : '',
            responsableId: tacheData.responsable ? tacheData.responsable.id.toString() : '',
            description: tacheData.description || '',
            etat: tacheData.etat || '',
            priorite: tacheData.priorite || '',
            deadline: tacheData.deadline || '',
            ressourceIds: tacheData.ressources ? tacheData.ressources.map(r => r.id.toString()) : []
          });
        }
        setProjets(projetData);
        setEmployes(employeData);
        console.log('Employes:', employeData);
        setRessources(ressourceData);
      })
      .catch(err => setError(err.message));
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setTache({ ...tache, [name]: value });
  };

  const handleAddRessource = () => {
    if (selectedRessource && !tache.ressourceIds.includes(selectedRessource)) {
      setTache(prev => ({
        ...prev,
        ressourceIds: [...prev.ressourceIds, selectedRessource]
      }));
      setSelectedRessource('');
    }
  };

  const handleRemoveRessource = (idToRemove) => {
    setTache(prev => ({
      ...prev,
      ressourceIds: prev.ressourceIds.filter(id => id !== idToRemove)
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!tache.nom) {
      setError("Le nom de la tâche est requis.");
      return;
    }
    if (!tache.projetId) {
      setError("Veuillez sélectionner un projet.");
      return;
    }
    if (!tache.responsableId) {
      setError("Veuillez sélectionner un responsable.");
      return;
    }
    if (!tache.deadline) {
      setError("La date limite est requise.");
      return;
    }

    const method = id ? 'PUT' : 'POST';
    const url = id ? `http://localhost:8080/api/taches/${id}` : 'http://localhost:8080/api/taches';
    const payload = {
      ...(method === 'POST' ? {
        nom: tache.nom,
        description: tache.description,
        etat: tache.etat || "En cours",
        priorite: tache.priorite || "Moyenne",
        deadline: tache.deadline,
        projetId: parseInt(tache.projetId),
        responsableId: parseInt(tache.responsableId),
        ressourceIds: tache.ressourceIds.map(id => parseInt(id))
      } : {
        etat: tache.etat || "En cours",
        priorite: tache.priorite || "Moyenne",
        deadline: tache.deadline,
        projetId: parseInt(tache.projetId)
      })
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
        fetch('http://localhost:8080/api/taches')
          .then(res => res.json())
          .then(data => console.log('Taches refreshed:', data));
        navigate('/taches');
      })
      .catch(err => setError(err.message || 'Erreur inattendue'));
  };

  return (
    <div className="max-w-2xl mx-auto p-6 bg-white shadow-lg rounded-lg">
      <h2 className="text-3xl font-bold mb-6 text-gray-800">{id ? 'Modifier Tache' : 'Ajouter Tache'}</h2>
      {error && <div className="text-red-500 mb-4">{error}</div>}
      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label className="block text-sm font-medium text-gray-700">Nom</label>
          <input
            type="text"
            name="nom"
            value={tache.nom}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required={!id}
            disabled={!!id}
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Projet</label>
          <select
            name="projetId"
            value={tache.projetId || ''}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required={!id}
            disabled={!!id}
          >
            <option value="">Sélectionner un projet</option>
            {projets.map(projet => (
              <option key={projet.id} value={projet.id.toString()}>{projet.nom}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Responsable</label>
          <select
            name="responsableId"
            value={tache.responsableId || ''}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required={!id}
            disabled={!!id}
          >
            <option value="">Sélectionner un employé</option>
            {employes.map(employe => (
              <option key={employe.id} value={employe.id.toString()}>
                {employe.nom} ({employe.role || 'Rôle non défini'})
              </option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Description</label>
          <input
            type="text"
            name="description"
            value={tache.description}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required={!id}
            disabled={!!id}
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">État</label>
          <select
            name="etat"
            value={tache.etat}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
          >
            <option value="">Sélectionner un état</option>
            {etatOptions.map(option => (
              <option key={option} value={option}>{option}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Priorité</label>
          <select
            name="priorite"
            value={tache.priorite}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
          >
            <option value="">Sélectionner une priorité</option>
            {prioriteOptions.map(option => (
              <option key={option} value={option}>{option}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Date Limite</label>
          <input
            type="date"
            name="deadline"
            value={tache.deadline || ''}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required
          />
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
              <option key={ressource.id} value={ressource.id.toString()}>{ressource.nom} ({ressource.type})</option>
            ))}
          </select>
          <button
            type="button"
            onClick={handleAddRessource}
            className="mt-2 px-3 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 flex items-center"
          >
            <FaPlus className="mr-2" /> Ajouter
          </button>
          {tache.ressourceIds.length > 0 && (
            <div className="mt-2">
              <p>Ressources sélectionnées :</p>
              <ul className="list-disc pl-5">
                {tache.ressourceIds.map(id => {
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

export default TacheForm;