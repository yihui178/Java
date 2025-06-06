<template>
    <div class="register-container">
        <div class="register-box">
            <!-- 左侧图片区域 -->
            <div class="register-left">
                <div class="overlay"></div>
                <img src="https://java-ai-178.oss-cn-beijing.aliyuncs.com/2025/assets/login1.png" alt="register background">
                <div class="welcome-text">
                    <h1>Join Us Today</h1>
                    <p>Create an account and start your journey with our warehouse management system</p>
                </div>
            </div>

            <!-- 右侧注册表单区域 -->
            <div class="register-right">
                <div class="register-form-container">
                    <h2>注册仓库管理系统</h2>
                    <p class="subtitle">请填写以下信息创建您的账号</p>

                    <el-form ref="registerForm" :model="formData" :rules="rules">
                        <el-form-item prop="UserName">
                            <el-input v-model.trim="formData.UserName" placeholder="请输入账号" prefix-icon="el-icon-user">
                            </el-input>
                        </el-form-item>

                        <el-form-item prop="Password">
                            <el-input type="password" v-model.trim="formData.Password" placeholder="请输入密码"
                                prefix-icon="el-icon-lock">
                            </el-input>
                        </el-form-item>

                        <el-form-item prop="ConfirmPassword">
                            <el-input type="password" v-model.trim="formData.ConfirmPassword" placeholder="请确认密码"
                                prefix-icon="el-icon-lock">
                            </el-input>
                        </el-form-item>



                        <el-form-item prop="Code" class="verify-code">
                            <div class="code-input-group">
                                <el-input v-model.trim="formData.Code" placeholder="请输入验证码">
                                </el-input>
                                <ValidCode ref="ValidCode" class="code-image"></ValidCode>
                            </div>
                        </el-form-item>

                        <el-form-item prop="agreement" class="agreement-item">
                            <el-checkbox v-model="formData.agreement">
                                我已阅读并同意
                                <span class="agreement-link" @click="showUserAgreement">《用户协议》</span>
                                和
                                <span class="agreement-link" @click="showPrivacyPolicy">《隐私政策》</span>
                            </el-checkbox>
                        </el-form-item>

                        <el-form-item>
                            <el-button type="primary" @click="RegisterBtn" class="register-btn">
                                注 册
                            </el-button>
                        </el-form-item>
                    </el-form>

                    <div class="form-footer">
                        <span class="login-link">
                            已有账号? <RouterLink :to="{ path: '/Login' }">立即登录</RouterLink>
                        </span>
                    </div>
                </div>
            </div>
        </div>

        <!-- 用户协议弹窗 -->
        <el-dialog title="用户协议" :visible.sync="userAgreementVisible" width="50%">
            <div class="agreement-content">
                <h3>用户协议</h3>
                <p>欢迎使用仓库管理系统，请您仔细阅读以下条款：</p>
                <p>1. 用户在注册和使用本系统时，必须遵守相关法律法规。</p>
                <p>2. 用户应当妥善保管账号和密码，对账号下的所有操作负责。</p>
                <p>3. 用户不得利用本系统进行任何违法或不当的活动。</p>
                <p>4. 本系统保留随时修改服务条款的权利，修改后的服务条款将在网站上公布。</p>
                <p>5. 如用户违反上述条款，本系统有权终止对该用户的服务。</p>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="userAgreementVisible = false">我已阅读</el-button>
            </span>
        </el-dialog>

        <!-- 隐私政策弹窗 -->
        <el-dialog title="隐私政策" :visible.sync="privacyPolicyVisible" width="50%">
            <div class="agreement-content">
                <h3>隐私政策</h3>
                <p>我们非常重视您的隐私保护，请您了解我们如何收集和使用您的信息：</p>
                <p>1. 我们收集的信息：当您注册账号时，我们会收集您提供的账号和密码等信息。</p>
                <p>2. 信息使用：我们使用收集的信息来提供、维护和改进我们的服务。</p>
                <p>3. 信息安全：我们采取合理的安全措施来保护您的个人信息。</p>
                <p>4. 信息共享：除非获得您的明确同意，我们不会与第三方共享您的个人信息。</p>
                <p>5. 您的权利：您有权访问、更正或删除您的个人信息。</p>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="privacyPolicyVisible = false">我已阅读</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import ValidCode from '@/components/Code/canvas.vue';

export default {
    components: {
        ValidCode: ValidCode
    },
    data() {
        // 密码一致性验证
        const validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.formData.Password) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };

        return {
            formData: {
                UserName: '',
                Password: '',
                ConfirmPassword: '',
                RoleType: "2",
                Code: "",
                agreement: false
            },
            roleOptions: [],
            userAgreementVisible: false,
            privacyPolicyVisible: false,
            rules: {
                UserName: [
                    { required: true, message: '请输入账号', trigger: 'blur' },
                    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
                ],
                Password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
                ],
                ConfirmPassword: [
                    { required: true, message: '请确认密码', trigger: 'blur' },
                    { validator: validatePass2, trigger: 'blur' }
                ],
                RoleType: [
                    { required: true, message: '请选择角色', trigger: 'blur' },
                ],
                Code: [
                    { required: true, message: '请输入验证码', trigger: 'blur' },
                    {
                        validator: (rule, value, callback) => {
                            let identifyCode = this.$refs.ValidCode.getCode();
                            if (value !== identifyCode) {
                                callback(new Error('请输入正确的验证码'));
                            } else {
                                callback();
                            }
                        }, trigger: 'blur'
                    }
                ],
                agreement: [
                    {
                        validator: (rule, value, callback) => {
                            if (!value) {
                                callback(new Error('请阅读并同意用户协议和隐私政策'));
                            } else {
                                callback();
                            }
                        },
                        trigger: 'change'
                    }
                ]
            }
        }
    },
    created() {
        this.GetRoleTypeApi();
    },
    methods: {
        async GetRoleTypeApi() {
            let { Data: { Items } } = await this.$Post("/Select/RoleType");
            this.roleOptions = Items;
        },
        RegisterBtn() {
            this.$refs.registerForm.validate(async (valid) => {
                if (valid) {
                    // 注册逻辑
                    let res = await this.$Post("/User/Register", this.formData);
                    if (res.Success) {
                        this.$message.success("注册成功!");
                        this.$router.push({
                            path: "/Login"
                        });
                    } else {
                        this.$refs.ValidCode.refreshCode();
                    }
                } else {
                    this.$message.error("注册验证不通过");
                    this.$refs.ValidCode.refreshCode();
                    return false;
                }
            });
        },
        showUserAgreement() {
            this.userAgreementVisible = true;
        },
        showPrivacyPolicy() {
            this.privacyPolicyVisible = true;
        }
    }
}
</script>

<style scoped>
/* 整体容器样式 */
.register-container {
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #1a237e 0%, #0d47a1 100%);
}

/* 注册框样式 */
.register-box {
    width: 1200px;
    height: 700px;
    /* 稍微高一点，适应更多表单项 */
    display: flex;
    border-radius: 20px;
    overflow: hidden;
    box-shadow: 0 15px 25px rgba(0, 0, 0, 0.2);
}

/* 左侧图片区域样式 */
.register-left {
    position: relative;
    flex: 1;
    background: #000;
    overflow: hidden;
}

.register-left img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    opacity: 0.7;
}

.overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(45deg, rgba(26, 35, 126, 0.7) 0%, rgba(13, 71, 161, 0.7) 100%);
    z-index: 1;
}

.welcome-text {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
    color: white;
    z-index: 2;
}

.welcome-text h1 {
    font-size: 2.5em;
    margin-bottom: 20px;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.welcome-text p {
    font-size: 1.1em;
    opacity: 0.9;
}

/* 右侧表单区域样式 */
.register-right {
    flex: 1;
    background: white;
    padding: 40px;
    display: flex;
    align-items: center;
}

.register-form-container {
    width: 100%;
    max-width: 400px;
    margin: 0 auto;
}

.register-form-container h2 {
    font-size: 1.8em;
    color: #333;
    margin-bottom: 10px;
}

.subtitle {
    color: #666;
    margin-bottom: 30px;
}

.el-input {
    margin-bottom: 20px;
}

.role-select {
    margin: 20px 0;
}

/* 验证码样式 */
.verify-code .code-input-group {
    display: flex;
    gap: 10px;
}

.code-image {
    flex-shrink: 0;
}

/* 注册按钮样式 */
.register-btn {
    width: 100%;
    height: 45px;
    font-size: 16px;
    margin-top: 20px;
    background: linear-gradient(45deg, #1a237e, #0d47a1);
    border: none;
}

.register-btn:hover {
    background: linear-gradient(45deg, #0d47a1, #1a237e);
    transform: translateY(-1px);
    transition: all 0.3s ease;
}

/* 底部链接样式 */
.form-footer {
    margin-top: 20px;
    text-align: center;
}

.login-link {
    color: #666;
    font-size: 14px;
}

.login-link a {
    color: #1a237e;
    text-decoration: none;
}

.login-link a:hover {
    text-decoration: underline;
}

/* 协议相关样式 */
.agreement-item {
    margin-top: 10px;
}

.agreement-link {
    color: #1a237e;
    cursor: pointer;
}

.agreement-link:hover {
    text-decoration: underline;
}

.agreement-content {
    max-height: 400px;
    overflow-y: auto;
    padding: 0 10px;
}

.agreement-content h3 {
    margin-bottom: 15px;
    color: #1a237e;
}

.agreement-content p {
    margin-bottom: 10px;
    line-height: 1.6;
}
</style>