let currentUserId = null;
let allProfilesCache = [];
let selectedChatUserId = null;
let selectedChatUserName = "";
let chatRefreshInterval = null;
let lastOpenedSection = "browseProfilesSection";

function getAuthHeaders() {
  const token = localStorage.getItem("token");

  return {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`
  };
}

function showSection(sectionId) {
  const sections = [
    "registerSection",
    "loginSection",
    "profileSection",
    "preferenceSection",
    "browseProfilesSection",
    "matchesSection",
    "messagesSection"
  ];

  sections.forEach(id => {
    const el = document.getElementById(id);
    if (el) el.style.display = "none";
  });

  const activeSection = document.getElementById(sectionId);
  if (activeSection) activeSection.style.display = "block";
}

function showLoggedInMenu() {
  document.getElementById("publicMenu").style.display = "none";
  document.getElementById("privateMenu").style.display = "flex";
}

function clearBrowseProfiles() {
  const list = document.getElementById("browseProfilesList");
  const message = document.getElementById("browseProfilesMessage");

  if (list) list.innerHTML = "";
  if (message) message.innerText = "";

  allProfilesCache = [];
}

function clearSectionMessages() {
  const messageIds = [
    "registerMessage",
    "loginMessage",
    "profileMessage",
    "preferenceMessage",
    "browseProfilesMessage",
    "matchesMessage",
    "messageBoxMessage"
  ];

  messageIds.forEach(id => {
    const el = document.getElementById(id);
    if (el) el.innerText = "";
  });
}

function clearMatches() {
  const list = document.getElementById("matchesList");
  const message = document.getElementById("matchesMessage");

  if (list) list.innerHTML = "";
  if (message) message.innerText = "";
}

function clearMessagesSection() {
  const conversationList = document.getElementById("conversationList");
  const messageContent = document.getElementById("messageContent");
  const chatWithUser = document.getElementById("chatWithUser");
  const messageBoxMessage = document.getElementById("messageBoxMessage");

  if (conversationList) conversationList.innerHTML = "";
  if (messageContent) messageContent.value = "";
  if (chatWithUser) chatWithUser.innerText = "";
  if (messageBoxMessage) messageBoxMessage.innerText = "";

  selectedChatUserId = null;
  selectedChatUserName = "";

  if (chatRefreshInterval) {
    clearInterval(chatRefreshInterval);
    chatRefreshInterval = null;
  }
}

function logoutUser() {
  currentUserId = null;
  localStorage.removeItem("token");

  if (chatRefreshInterval) {
    clearInterval(chatRefreshInterval);
    chatRefreshInterval = null;
  }

  document.getElementById("privateMenu").style.display = "none";
  document.getElementById("publicMenu").style.display = "flex";

  clearProfileForm();
  clearPreferenceForm();
  clearLoginForm();
  clearRegisterForm();
  clearSectionMessages();
  clearBrowseProfiles();
  clearMatches();
  clearMessagesSection();

  showSection("loginSection");
}

function clearLoginForm() {
  const email = document.getElementById("loginEmail");
  const password = document.getElementById("loginPassword");
  if (email) email.value = "";
  if (password) password.value = "";
}

function clearRegisterForm() {
  const firstName = document.getElementById("registerFirstName");
  const lastName = document.getElementById("registerLastName");
  const email = document.getElementById("registerEmail");
  const password = document.getElementById("registerPassword");

  if (firstName) firstName.value = "";
  if (lastName) lastName.value = "";
  if (email) email.value = "";
  if (password) password.value = "";
}

function clearProfileForm() {
  const age = document.getElementById("age");
  const gender = document.getElementById("gender");
  const city = document.getElementById("city");
  const bio = document.getElementById("bio");
  const height = document.getElementById("height");

  if (age) age.value = "";
  if (gender) gender.value = "";
  if (city) city.value = "";
  if (bio) bio.value = "";
  if (height) height.value = "";
}

function clearPreferenceForm() {
  const preferredGender = document.getElementById("preferredGender");
  const minAge = document.getElementById("minAge");
  const maxAge = document.getElementById("maxAge");
  const preferredCity = document.getElementById("preferredCity");

  if (preferredGender) preferredGender.value = "";
  if (minAge) minAge.value = "";
  if (maxAge) maxAge.value = "";
  if (preferredCity) preferredCity.value = "";
}

async function registerUser() {
  const firstName = document.getElementById("registerFirstName").value.trim();
  const lastName = document.getElementById("registerLastName").value.trim();
  const email = document.getElementById("registerEmail").value.trim();
  const password = document.getElementById("registerPassword").value.trim();
  const messageEl = document.getElementById("registerMessage");

  try {
    const response = await fetch("/api/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ firstName, lastName, email, password })
    });

    const data = await response.json();

    if (!response.ok) throw new Error(data.message || "Registration failed");

    messageEl.innerText = "Registration successful! Please login.";
    messageEl.style.color = "green";
    clearRegisterForm();
    showSection("loginSection");
  } catch (error) {
    messageEl.innerText = "Error: " + error.message;
    messageEl.style.color = "red";
  }
}

async function loginUser() {
  const email = document.getElementById("loginEmail").value.trim();
  const password = document.getElementById("loginPassword").value.trim();
  const messageEl = document.getElementById("loginMessage");

  try {
    const response = await fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });

    const data = await response.json();

    if (!response.ok) throw new Error(data.message || "Login failed");

    currentUserId = data.id;
    localStorage.setItem("token", data.token);

    messageEl.innerText = "Login successful!";
    messageEl.style.color = "green";

    showLoggedInMenu();
    showSection("browseProfilesSection");

    await getProfile();
    await getPreference();
    await loadAllProfiles();
  } catch (error) {
    messageEl.innerText = "Error: " + error.message;
    messageEl.style.color = "red";
  }
}

async function saveProfile() {
  const age = document.getElementById("age").value;
  const gender = document.getElementById("gender").value;
  const city = document.getElementById("city").value.trim();
  const bio = document.getElementById("bio").value.trim();
  const height = document.getElementById("height").value;
  const messageEl = document.getElementById("profileMessage");

  if (!localStorage.getItem("token")) {
    messageEl.innerText = "Please login first.";
    messageEl.style.color = "red";
    return;
  }

  try {
    const response = await fetch("/api/profile/me", {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify({
        age: age ? parseInt(age, 10) : null,
        gender,
        city,
        bio,
        height: height ? parseInt(height, 10) : null
      })
    });

    const data = await response.json();
    if (!response.ok) throw new Error(data.message || "Failed to save profile");

    currentUserId = data.userId || currentUserId;
    messageEl.innerText = "Profile saved successfully!";
    messageEl.style.color = "green";
  } catch (error) {
    messageEl.innerText = "Error saving profile: " + error.message;
    messageEl.style.color = "red";
  }
}

async function getProfile() {
  const messageEl = document.getElementById("profileMessage");

  if (!localStorage.getItem("token")) {
    messageEl.innerText = "Please login first.";
    messageEl.style.color = "red";
    return;
  }

  try {
    const response = await fetch("/api/profile/me", {
      headers: getAuthHeaders()
    });

    if (!response.ok) {
      clearProfileForm();
      messageEl.innerText = "No profile found. Please fill your profile details.";
      messageEl.style.color = "orange";
      return;
    }

    const data = await response.json();
    currentUserId = data.userId || currentUserId;

    document.getElementById("age").value = data.age || "";
    document.getElementById("gender").value = data.gender || "";
    document.getElementById("city").value = data.city || "";
    document.getElementById("bio").value = data.bio || "";
    document.getElementById("height").value = data.height || "";

    messageEl.innerText = "Profile loaded successfully!";
    messageEl.style.color = "green";
  } catch (error) {
    clearProfileForm();
    messageEl.innerText = "Unable to load profile. Please fill your profile details.";
    messageEl.style.color = "orange";
  }
}

async function savePreference() {
  const preferredGender = document.getElementById("preferredGender").value;
  const minAge = document.getElementById("minAge").value;
  const maxAge = document.getElementById("maxAge").value;
  const preferredCity = document.getElementById("preferredCity").value.trim();
  const messageEl = document.getElementById("preferenceMessage");

  if (!localStorage.getItem("token")) {
    messageEl.innerText = "Please login first.";
    messageEl.style.color = "red";
    return;
  }

  try {
    const response = await fetch("/api/preferences/me", {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify({
        preferredGender,
        minAge: minAge ? parseInt(minAge, 10) : null,
        maxAge: maxAge ? parseInt(maxAge, 10) : null,
        city: preferredCity
      })
    });

    const data = await response.json();
    if (!response.ok) throw new Error(data.message || "Failed to save preference");

    messageEl.innerText = "Preference saved successfully!";
    messageEl.style.color = "green";
  } catch (error) {
    messageEl.innerText = "Error saving preference: " + error.message;
    messageEl.style.color = "red";
  }
}

async function getPreference() {
  const messageEl = document.getElementById("preferenceMessage");

  if (!localStorage.getItem("token")) {
    messageEl.innerText = "Please login first.";
    messageEl.style.color = "red";
    return;
  }

  try {
    const response = await fetch("/api/preferences/me", {
      headers: getAuthHeaders()
    });

    if (!response.ok) {
      clearPreferenceForm();
      messageEl.innerText = "No preference found. Please fill your preference details.";
      messageEl.style.color = "orange";
      return;
    }

    const data = await response.json();
    document.getElementById("preferredGender").value = data.preferredGender || "";
    document.getElementById("minAge").value = data.minAge || "";
    document.getElementById("maxAge").value = data.maxAge || "";
    document.getElementById("preferredCity").value = data.city || "";

    messageEl.innerText = "Preference loaded successfully!";
    messageEl.style.color = "green";
  } catch (error) {
    clearPreferenceForm();
    messageEl.innerText = "Unable to load preference. Please fill your preference details.";
    messageEl.style.color = "orange";
  }
}

async function loadAllProfiles() {
  const messageEl = document.getElementById("browseProfilesMessage");
  const listEl = document.getElementById("browseProfilesList");

  if (!currentUserId) {
    messageEl.innerText = "Please login first.";
    messageEl.style.color = "red";
    return;
  }

  listEl.innerHTML = "";
  messageEl.innerText = "Loading profiles...";
  messageEl.style.color = "#555";

  try {
    const response = await fetch("/api/profile/all", {
      headers: getAuthHeaders()
    });

    if (!response.ok) throw new Error(await response.text() || "Failed to load profiles");

    const data = await response.json();
    allProfilesCache = Array.isArray(data)
      ? data.filter(profile => profile.userId !== currentUserId)
      : [];

    renderProfiles(allProfilesCache);
  } catch (error) {
    messageEl.innerText = "Error loading profiles: " + error.message;
    messageEl.style.color = "red";
  }
}

function renderProfiles(profiles) {
  const messageEl = document.getElementById("browseProfilesMessage");
  const listEl = document.getElementById("browseProfilesList");
  listEl.innerHTML = "";

  if (!Array.isArray(profiles) || profiles.length === 0) {
    messageEl.innerText = "No profiles found.";
    messageEl.style.color = "orange";
    return;
  }

  messageEl.innerText = `Found ${profiles.length} profile(s).`;
  messageEl.style.color = "green";

  profiles.forEach(profile => {
    const fullName = `${profile.firstName || ""} ${profile.lastName || ""}`.trim();
    const card = document.createElement("div");
    card.className = "match-card";
    card.innerHTML = `
      <h3>${fullName}</h3>
      <p><strong>Age:</strong> ${profile.age || ""}</p>
      <p><strong>Gender:</strong> ${profile.gender || ""}</p>
      <p><strong>City:</strong> ${profile.city || ""}</p>
      <p><strong>Height:</strong> ${profile.height || ""}</p>
      <p><strong>Bio:</strong> ${profile.bio || ""}</p>
      <div class="action-buttons">
        <button type="button" onclick='openChat(${profile.userId}, ${JSON.stringify(fullName)}, "browseProfilesSection")'>Message</button>
      </div>
    `;
    listEl.appendChild(card);
  });
}

function applyProfileFilters() {
  const gender = document.getElementById("filterGender").value.trim();
  const minAgeValue = document.getElementById("filterMinAge").value.trim();
  const maxAgeValue = document.getElementById("filterMaxAge").value.trim();
  const city = document.getElementById("filterCity").value.trim().toLowerCase();

  const minAge = minAgeValue ? parseInt(minAgeValue, 10) : null;
  const maxAge = maxAgeValue ? parseInt(maxAgeValue, 10) : null;

  const filtered = allProfilesCache.filter(profile => {
    const profileGender = (profile.gender || "").trim();
    const profileAge = profile.age;
    const profileCity = (profile.city || "").trim().toLowerCase();

    if (gender && profileGender !== gender) return false;
    if (minAge !== null && profileAge < minAge) return false;
    if (maxAge !== null && profileAge > maxAge) return false;
    if (city && !profileCity.includes(city)) return false;

    return true;
  });

  renderProfiles(filtered);
}

function resetProfileFilters() {
  document.getElementById("filterGender").value = "";
  document.getElementById("filterMinAge").value = "";
  document.getElementById("filterMaxAge").value = "";
  document.getElementById("filterCity").value = "";
  renderProfiles(allProfilesCache);
}

async function loadMatches() {
  const messageEl = document.getElementById("matchesMessage");
  const listEl = document.getElementById("matchesList");

  if (!currentUserId) {
    messageEl.innerText = "Please login first.";
    messageEl.style.color = "red";
    return;
  }

  listEl.innerHTML = "";
  messageEl.innerText = "Loading matches...";
  messageEl.style.color = "#555";

  try {
    const response = await fetch(`/api/matches/${currentUserId}`, {
      headers: getAuthHeaders()
    });

    if (!response.ok) throw new Error(await response.text() || "Failed to load matches");

    const data = await response.json();

    if (!Array.isArray(data) || data.length === 0) {
      messageEl.innerText = "No matches found yet.";
      messageEl.style.color = "orange";
      return;
    }

    messageEl.innerText = `Found ${data.length} match(es).`;
    messageEl.style.color = "green";

    data.forEach(match => {
      const fullName = `${match.firstName || ""} ${match.lastName || ""}`.trim();
      const card = document.createElement("div");
      card.className = "match-card";
      card.innerHTML = `
        <h3>${fullName}</h3>
        <p><strong>Age:</strong> ${match.age || ""}</p>
        <p><strong>Gender:</strong> ${match.gender || ""}</p>
        <p><strong>City:</strong> ${match.city || ""}</p>
        <p><strong>Bio:</strong> ${match.bio || ""}</p>
        <div class="action-buttons">
          <button type="button" onclick='openChat(${match.userId}, ${JSON.stringify(fullName)}, "matchesSection")'>Message</button>
        </div>
      `;
      listEl.appendChild(card);
    });
  } catch (error) {
    messageEl.innerText = "Error loading matches: " + error.message;
    messageEl.style.color = "red";
  }
}

function openChat(otherUserId, otherUserName, sourceSection) {
  selectedChatUserId = otherUserId;
  selectedChatUserName = otherUserName;
  lastOpenedSection = sourceSection || "browseProfilesSection";

  document.getElementById("chatWithUser").innerText = `Chat with ${otherUserName}`;
  document.getElementById("messageBoxMessage").innerText = "";
  document.getElementById("messageContent").value = "";

  showSection("messagesSection");
  refreshConversation();

  if (chatRefreshInterval) clearInterval(chatRefreshInterval);

  chatRefreshInterval = setInterval(() => {
    if (selectedChatUserId && currentUserId) refreshConversation();
  }, 5000);
}

function goBackFromChat() {
  if (chatRefreshInterval) {
    clearInterval(chatRefreshInterval);
    chatRefreshInterval = null;
  }

  showSection(lastOpenedSection);

  if (lastOpenedSection === "browseProfilesSection") {
    loadAllProfiles();
  } else if (lastOpenedSection === "matchesSection") {
    loadMatches();
  }
}

function formatDateTime(dateTimeString) {
  if (!dateTimeString) return "";

  const date = new Date(dateTimeString);
  return date.toLocaleString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "numeric",
    minute: "2-digit",
    hour12: true
  });
}

async function refreshConversation() {
  const messageEl = document.getElementById("messageBoxMessage");
  const listEl = document.getElementById("conversationList");

  if (!currentUserId || !selectedChatUserId) {
    messageEl.innerText = "No chat selected.";
    messageEl.style.color = "red";
    return;
  }

  listEl.innerHTML = "<p>Loading conversation...</p>";

  try {
    const response = await fetch(`/api/messages/${selectedChatUserId}`, {
      headers: getAuthHeaders()
    });

    if (!response.ok) throw new Error(await response.text() || "Failed to load conversation");

    const data = await response.json();
    listEl.innerHTML = "";

    if (!Array.isArray(data) || data.length === 0) {
      listEl.innerHTML = `<div class="empty-chat">No messages yet. Start the conversation.</div>`;
      return;
    }

    data.forEach(msg => {
      const msgDiv = document.createElement("div");
      msgDiv.className = msg.senderId === currentUserId ? "chat-row sent-row" : "chat-row received-row";
      msgDiv.innerHTML = `
        <div class="chat-bubble ${msg.senderId === currentUserId ? "sent" : "received"}">
          <div class="chat-text">${escapeHtml(msg.content)}</div>
          <div class="chat-time">${formatDateTime(msg.sentAt)}</div>
        </div>
      `;
      listEl.appendChild(msgDiv);
    });

    listEl.scrollTop = listEl.scrollHeight;
  } catch (error) {
    messageEl.innerText = "Error loading conversation: " + error.message;
    messageEl.style.color = "red";
  }
}

async function sendMessage() {
  const messageEl = document.getElementById("messageBoxMessage");
  const content = document.getElementById("messageContent").value.trim();

  if (!currentUserId || !selectedChatUserId) {
    messageEl.innerText = "No chat selected.";
    messageEl.style.color = "red";
    return;
  }

  if (!content) {
    messageEl.innerText = "Please type a message.";
    messageEl.style.color = "red";
    return;
  }

  try {
    const response = await fetch("/api/messages", {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify({
        receiverId: selectedChatUserId,
        content
      })
    });

    const text = await response.text();
    let data = {};
    try {
      data = text ? JSON.parse(text) : {};
    } catch {
      data = { message: text };
    }

    if (!response.ok) throw new Error(data.message || "Failed to send message");

    document.getElementById("messageContent").value = "";
    messageEl.innerText = "Message sent successfully!";
    messageEl.style.color = "green";

    await refreshConversation();
  } catch (error) {
    messageEl.innerText = "Error sending message: " + error.message;
    messageEl.style.color = "red";
  }
}

function escapeHtml(value) {
  if (value === null || value === undefined) return "";

  return String(value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;");
}

document.addEventListener("DOMContentLoaded", () => {
  const textarea = document.getElementById("messageContent");

  if (textarea) {
    textarea.addEventListener("keydown", function (event) {
      if (event.key === "Enter" && !event.shiftKey) {
        event.preventDefault();
        sendMessage();
      }
    });
  }
});
